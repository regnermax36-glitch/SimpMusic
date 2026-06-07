package com.maxrave.maxregneplayer

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
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
import java.nio.ByteBuffer

/**
 * Android music player built on raw Android SDK only.
 *
 * Pipeline:
 *   HTTP/HTTPS stream → MediaExtractor (demux) → MediaCodec (decode)
 *   → AudioTrack (PCM output)
 *
 * Volume is applied per-write via a linear float multiplier, giving us
 * software crossfade capability without any third-party library.
 */
class MrAndroidPlayer : MrPlayer {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val queue = MrQueue()
    private val _state = MutableStateFlow(MrPlayerState())
    override val state: StateFlow<MrPlayerState> = _state.asStateFlow()

    // Active decode/playback job
    private var playbackJob: Job? = null
    private var crossfadeJob: Job? = null

    // Volatile for safe cross-thread read in the decode loop
    @Volatile private var volumeGain: Float = 1f
    @Volatile private var speedFactor: Float = 1f
    @Volatile private var paused: Boolean = false
    @Volatile private var stopRequested: Boolean = false
    @Volatile private var crossfadeSecs: Int = 0

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

    override fun play()      { paused = false; _state.update { it.copy(isPlaying = true,  status = MrPlaybackStatus.Playing) } }
    override fun pause()     { paused = true;  _state.update { it.copy(isPlaying = false, status = MrPlaybackStatus.Paused) } }
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
        // Signal the decode loop to seek; the loop checks seekTargetMs
        seekTargetMs = ms.coerceAtLeast(0L)
        _state.update { it.copy(positionMs = ms) }
    }

    override fun seekBy(deltaMs: Long) = seekTo(_state.value.positionMs + deltaMs)

    @Volatile private var seekTargetMs: Long = -1L

    // ── Settings ──────────────────────────────────────────────────────────

    override fun setRepeatMode(mode: MrRepeatMode) { _state.update { it.copy(repeatMode = mode) } }

    override fun setShuffle(on: Boolean) {
        queue.shuffle(on); _state.update { it.copy(isShuffleOn = on, queue = queue.tracks) }
    }

    override fun setVolume(v: Float) {
        volumeGain = v.coerceIn(0f, 1f); _state.update { it.copy(volume = volumeGain) }
    }

    override fun setSpeed(s: Float) {
        speedFactor = s.coerceIn(0.5f, 2.0f); _state.update { it.copy(speed = speedFactor) }
    }

    override fun setCrossfade(seconds: Int) {
        crossfadeSecs = seconds.coerceIn(0, 15)
        _state.update { it.copy(crossfadeSecs = crossfadeSecs) }
    }

    override fun release() {
        stopRequested = true
        playbackJob?.cancel()
        crossfadeJob?.cancel()
        scope.cancel()
    }

    // ── Decode loop ───────────────────────────────────────────────────────

    private fun startPlayback(track: MrTrack) {
        stopRequested = false
        playbackJob?.cancel()
        paused = false
        seekTargetMs = -1L
        _state.update {
            it.copy(
                currentTrack = track,
                status = MrPlaybackStatus.Loading,
                isPlaying = true,
                positionMs = 0L,
                durationMs = track.durationMs ?: 0L,
            )
        }
        playbackJob = scope.launch { decodeAndPlay(track) }
    }

    private suspend fun decodeAndPlay(track: MrTrack) = withContext(Dispatchers.IO) {
        val extractor = MediaExtractor()
        var codec: MediaCodec? = null
        var audioTrack: AudioTrack? = null

        try {
            extractor.setDataSource(track.streamUrl)

            // Find first audio track
            var audioIndex = -1
            var format: MediaFormat? = null
            for (i in 0 until extractor.trackCount) {
                val f = extractor.getTrackFormat(i)
                if (f.getString(MediaFormat.KEY_MIME)?.startsWith("audio/") == true) {
                    audioIndex = i; format = f; break
                }
            }
            if (audioIndex < 0 || format == null) {
                _state.update { it.copy(status = MrPlaybackStatus.Error, error = "No audio track found") }
                return@withContext
            }

            extractor.selectTrack(audioIndex)
            val mime     = format.getString(MediaFormat.KEY_MIME)!!
            val sampleHz = format.getInteger(MediaFormat.KEY_SAMPLE_RATE)
            val channels = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
            val duration = if (format.containsKey(MediaFormat.KEY_DURATION))
                format.getLong(MediaFormat.KEY_DURATION) / 1_000L else 0L

            _state.update { it.copy(durationMs = if (duration > 0) duration else it.durationMs) }

            codec = MediaCodec.createDecoderByType(mime)
            codec.configure(format, null, null, 0)
            codec.start()

            val channelConfig = if (channels == 2) AudioFormat.CHANNEL_OUT_STEREO
                                else AudioFormat.CHANNEL_OUT_MONO
            val encoding = AudioFormat.ENCODING_PCM_16BIT
            val minBuf   = AudioTrack.getMinBufferSize(sampleHz, channelConfig, encoding)
            val bufSize  = maxOf(minBuf, 65536)

            audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setSampleRate(sampleHz)
                        .setChannelMask(channelConfig)
                        .setEncoding(encoding)
                        .build(),
                )
                .setBufferSizeInBytes(bufSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack.play()
            _state.update { it.copy(status = MrPlaybackStatus.Playing) }

            val info    = MediaCodec.BufferInfo()
            var inputEos  = false
            var outputEos = false
            val timeoutUs = 10_000L

            while (!outputEos && !stopRequested && isActive) {
                // Handle pause
                while (paused && !stopRequested && isActive) delay(50)
                if (stopRequested || !isActive) break

                // Handle seek
                val seekTarget = seekTargetMs
                if (seekTarget >= 0L) {
                    extractor.seekTo(seekTarget * 1_000L, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                    codec.flush()
                    seekTargetMs = -1L
                    inputEos = false
                }

                // Feed input
                if (!inputEos) {
                    val inIdx = codec.dequeueInputBuffer(timeoutUs)
                    if (inIdx >= 0) {
                        val buf = codec.getInputBuffer(inIdx)!!
                        val read = extractor.readSampleData(buf, 0)
                        if (read < 0) {
                            codec.queueInputBuffer(inIdx, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                            inputEos = true
                        } else {
                            val pts = extractor.sampleTime
                            codec.queueInputBuffer(inIdx, 0, read, pts, 0)
                            extractor.advance()
                            _state.update { it.copy(positionMs = pts / 1_000L) }
                        }
                    }
                }

                // Drain output
                val outIdx = codec.dequeueOutputBuffer(info, timeoutUs)
                when {
                    outIdx >= 0 -> {
                        if (info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                            outputEos = true
                        }
                        val outBuf = codec.getOutputBuffer(outIdx)
                        if (outBuf != null && info.size > 0) {
                            val pcm = ByteArray(info.size)
                            outBuf.position(info.offset)
                            outBuf.get(pcm, 0, info.size)

                            // Apply software volume
                            applyVolume(pcm, volumeGain)

                            audioTrack.write(pcm, 0, pcm.size)
                        }
                        codec.releaseOutputBuffer(outIdx, false)
                    }
                    outIdx == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED -> { /* format change, ignore */ }
                }
            }

            // Auto-advance on natural end
            if (outputEos && !stopRequested) {
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
            codec?.stop(); codec?.release()
            audioTrack?.stop(); audioTrack?.release()
            extractor.release()
        }
    }

    // Apply linear volume gain to 16-bit PCM samples in-place
    private fun applyVolume(pcm: ByteArray, gain: Float) {
        if (gain >= 1f) return
        var i = 0
        while (i < pcm.size - 1) {
            val sample = (pcm[i].toInt() and 0xFF) or (pcm[i + 1].toInt() shl 8)
            val scaled = (sample * gain).toInt().coerceIn(-32768, 32767)
            pcm[i]     = (scaled and 0xFF).toByte()
            pcm[i + 1] = (scaled shr 8).toByte()
            i += 2
        }
    }
}
