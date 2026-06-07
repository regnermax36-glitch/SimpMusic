package com.maxrave.maxregneplayer

import kotlinx.coroutines.flow.StateFlow

/**
 * Platform-agnostic music player.
 *
 * Android → [MrAndroidPlayer]  (MediaCodec + MediaExtractor + AudioTrack)
 * Desktop → [MrDesktopPlayer]  (javax.sound.sampled + HTTP stream)
 */
interface MrPlayer {

    val state: StateFlow<MrPlayerState>

    // Queue
    fun setQueue(tracks: List<MrTrack>, startIndex: Int = 0)
    fun playNext(track: MrTrack)
    fun addToQueue(track: MrTrack)
    fun removeFromQueue(index: Int)
    fun skipTo(index: Int)

    // Transport
    fun play()
    fun pause()
    fun playPause()
    fun next()
    fun previous()
    fun seekTo(ms: Long)
    fun seekBy(deltaMs: Long)

    // Settings
    fun setRepeatMode(mode: MrRepeatMode)
    fun setShuffle(enabled: Boolean)
    fun setVolume(v: Float)          // 0f..1f
    fun setSpeed(s: Float)           // 0.5f..2.0f
    fun setCrossfade(seconds: Int)   // 0 = off

    fun release()
}
