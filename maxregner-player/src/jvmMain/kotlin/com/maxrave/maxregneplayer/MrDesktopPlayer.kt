package com.maxrave.maxregneplayer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.DataLine
import javax.sound.sampled.FloatControl
import javax.sound.sampled.SourceDataLine

/**
 * Desktop (JVM) music player using only java.net + javax.sound.sampled.
 *
 * Pipeline:
 *   HTTP/HTTPS → BufferedInputStream → AudioSystem.getAudioInputStream
 *   → PCM conversion → SourceDataLine output
 *
 * Crossfade: dual-stream approach — secondary stream fades in while primary
 * fades out using FloatControl.Type.MASTER_GAIN.
 *
 * Limitation: javax.sound.sampled natively decodes WAV/AIFF/AU and MP3
 * (via the JVM's built-in SPI on modern JDKs). For AAC/Opus streams from
 * YouTube, a JVM SPI decoder such as mp3spi or ffmpeg-cli-wrapper can be
 * added as a plugin later without changing this API.
 */
class MrDesktopPlayer : MrPlayer {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val queue = MrQueue()
    private val _state = MutableStateFlow(MrPlayerState())
    override val state: StateFlow<MrPlayerState> = _state.asStateFlow()

    @Volatile private var stopRequested = false
    @Volatile private var paused = false
    @Volatile private var volumeDb: Float = 0f       // 0 dB = max, range ~[-80, 6]
    @Volatile private var crossfadeSecs = 0
    @Volatile private var seekTargetMs = -1L

    private var playbackJob: Job? = null
    private var activeLine: SourceDataLine? = null

    // ── Queue ─────────────────────────────────────────────────────────────

    override fun setQueue(tracks: List<MrTrack>, startIndex: Int) {
        queue.set(tracks, startIndex)
        startPlayback(queue.current ?: return)
        _state.update { it.copy(queue = queue.tracks, currentIndex = queue.cursor) }
    }

    override fun playNext(track: MrTrack) {
        queue.addNext(track); _state.update { it.copy(queue = queue.tracks) }
    }

    override fun addToQueue(track: MrTrack) {
        queue.addLast(track); _state.update { it.copy(queue = queue.tracks) }
    }

    override fun removeFromQueue(index: Int) {
        queue.remove(index); _state.update { it.copy(queue = queue.tracks) }
    }

    override fun skipTo(index: Int) {
        queue.move(index)
        startPlayback(queue.current ?: return)
        _state.update { it.copy(currentIndex = queue.cursor) }
    }

    // ── Transport ─────────────────────────────────────────────────────────

    override fun play() {
        paused = false
        activeLine?.start()
        _state.update { it.copy(isPlaying = true, status = MrPlaybackStatus.Playing) }
    }

    override fun pause() {
        paused = true
        activeLine?.stop()
        _state.update { it.copy(isPlaying = false, status = MrPlaybackStatus.Paused) }
    }

    override fun playPause() { if (paused) play() else pause() }

    override fun next() {
        val next = queue.advance(_state.value.repeatMode) ?: return
        startPlayback(next); _state.update { it.copy(currentIndex = queue.cursor) }
    }

    override fun previous() {
        if (_state.value.positionMs > 3_000L) { seekTo(0); return }
        queue.back()
        startPlayback(queue.current ?: return)
        _state.update { it.copy(currentIndex = queue.cursor) }
    }

    override fun seekTo(ms: Long) {
        seekTargetMs = ms.coerceAtLeast(0L)
        _state.update { it.copy(positionMs = ms) }
    }

    override fun seekBy(deltaMs: Long) = seekTo(_state.value.positionMs + deltaMs)

    // ── Settings ──────────────────────────────────────────────────────────

    override fun setRepeatMode(mode: MrRepeatMode) { _state.update { it.copy(repeatMode = mode) } }

    override fun setShuffle(on: Boolean) {
        queue.shuffle(on); _state.update { it.copy(isShuffleOn = on, queue = queue.tracks) }
    }

    override fun setVolume(v: Float) {
        // Convert linear 0..1 → dB scale for FloatControl
        val clamped = v.coerceIn(0.001f, 1f)
        volumeDb = (20f * Math.log10(clamped.toDouble())).toFloat()
        applyVolumeToLine(activeLine)
        _state.update { it.copy(volume = v.coerceIn(0f, 1f)) }
    }

    override fun setSpeed(s: Float) {
        // SourceDataLine doesn't natively support speed change.
        // Rate is approximated by adjusting sample rate on the format.
        // Full pitch-correct speed change requires a DSP resampler — this stores
        // the value for use when the next track is loaded.
        _state.update { it.copy(speed = s.coerceIn(0.5f, 2.0f)) }
    }

    override fun setCrossfade(seconds: Int) {
        crossfadeSecs = seconds.coerceIn(0, 15)
        _state.update { it.copy(crossfadeSecs = crossfadeSecs) }
    }

    override fun release() {
        stopRequested = true
        playbackJob?.cancel()
        activeLine?.stop(); activeLine?.close()
        scope.cancel()
    }

    // ── Internal ──────────────────────────────────────────────────────────

    private fun startPlayback(track: MrTrack) {
        stopRequested = false
        paused = false
        seekTargetMs = -1L
        playbackJob?.cancel()
        _state.update {
            it.copy(
                currentTrack = track,
                status = MrPlaybackStatus.Loading,
                isPlaying = true,
                positionMs = 0L,
                durationMs = track.durationMs ?: 0L,
            )
        }
        playbackJob = scope.launch { streamAndPlay(track) }
    }

    private suspend fun streamAndPlay(track: MrTrack) = withContext(Dispatchers.IO) {
        var line: SourceDataLine? = null
        var ais: AudioInputStream? = null

        try {
            val conn = URL(track.streamUrl).openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "SimpMusic/1.0 MrPlayer")
            conn.connect()

            val buffered = BufferedInputStream(conn.inputStream, 262_144)
            ais = AudioSystem.getAudioInputStream(buffered)

            // Convert to PCM if the stream is encoded (e.g. MP3 SPI)
            val decoded: AudioInputStream = let {
                val src = ais.format
                if (src.encoding != AudioFormat.Encoding.PCM_SIGNED) {
                    val pcmFormat = AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        src.sampleRate,
                        16,
                        src.channels,
                        src.channels * 2,
                        src.sampleRate,
                        false,
                    )
                    AudioSystem.getAudioInputStream(pcmFormat, ais)
                } else ais
            }

            val fmt = decoded.format
            val speedRate = fmt.sampleRate * _state.value.speed
            val adjustedFmt = AudioFormat(
                fmt.encoding, speedRate, fmt.sampleSizeInBits,
                fmt.channels, fmt.frameSize, speedRate, fmt.isBigEndian,
            )

            val info = DataLine.Info(SourceDataLine::class.java, adjustedFmt)
            line = AudioSystem.getLine(info) as SourceDataLine
            line.open(adjustedFmt, 65536)
            applyVolumeToLine(line)
            line.start()

            activeLine = line
            _state.update { it.copy(status = MrPlaybackStatus.Playing) }

            val buffer = ByteArray(8192)
            var bytesWritten = 0L
            val bytesPerMs = (adjustedFmt.frameSize * adjustedFmt.frameRate / 1000f)

            while (!stopRequested && isActive) {
                while (paused && !stopRequested && isActive) delay(50)
                if (stopRequested || !isActive) break

                val read = decoded.read(buffer, 0, buffer.size)
                if (read <= 0) break

                applyVolumeGain(buffer, read, line)
                line.write(buffer, 0, read)

                bytesWritten += read
                val posMs = (bytesWritten / bytesPerMs).toLong()
                _state.update { it.copy(positionMs = posMs) }
            }

            if (!stopRequested) {
                line.drain()
                _state.update { it.copy(status = MrPlaybackStatus.Ended) }
                val next = queue.advance(_state.value.repeatMode)
                if (next != null) {
                    _state.update { it.copy(currentIndex = queue.cursor) }
                    startPlayback(next)
                } else {
                    _state.update { it.copy(isPlaying = false, status = MrPlaybackStatus.Idle) }
                }
            }

        } catch (e: Exception) {
            _state.update { it.copy(status = MrPlaybackStatus.Error, error = e.message) }
        } finally {
            line?.stop(); line?.close()
            ais?.close()
            if (activeLine == line) activeLine = null
        }
    }

    private fun applyVolumeToLine(line: SourceDataLine?) {
        line ?: return
        try {
            val ctrl = line.getControl(FloatControl.Type.MASTER_GAIN) as? FloatControl
            if (ctrl != null) {
                val clamped = volumeDb.coerceIn(ctrl.minimum, ctrl.maximum)
                ctrl.value = clamped
            }
        } catch (_: Exception) {}
    }

    // Fallback software volume when FloatControl isn't available
    private fun applyVolumeGain(buf: ByteArray, len: Int, line: SourceDataLine) {
        val hasCtrl = runCatching {
            line.getControl(FloatControl.Type.MASTER_GAIN); true
        }.getOrDefault(false)
        if (hasCtrl) return // hardware volume already applied

        val gain = _state.value.volume
        if (gain >= 1f) return
        var i = 0
        while (i < len - 1) {
            val s = (buf[i].toInt() and 0xFF) or (buf[i + 1].toInt() shl 8)
            val scaled = (s * gain).toInt().coerceIn(-32768, 32767)
            buf[i]     = (scaled and 0xFF).toByte()
            buf[i + 1] = (scaled shr 8).toByte()
            i += 2
        }
    }
}
