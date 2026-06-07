package com.maxrave.maxregneplayer

enum class MrPlaybackStatus { Idle, Loading, Buffering, Ready, Playing, Paused, Ended, Error }
enum class MrRepeatMode    { Off, All, One }

data class MrPlayerState(
    val status:           MrPlaybackStatus = MrPlaybackStatus.Idle,
    val currentTrack:     MrTrack?         = null,
    val queue:            List<MrTrack>    = emptyList(),
    val currentIndex:     Int              = 0,
    val positionMs:       Long             = 0L,
    val durationMs:       Long             = 0L,
    val bufferedMs:       Long             = 0L,
    val isPlaying:        Boolean          = false,
    val repeatMode:       MrRepeatMode     = MrRepeatMode.Off,
    val isShuffleOn:      Boolean          = false,
    val volume:           Float            = 1f,
    val speed:            Float            = 1f,
    val crossfadeSecs:    Int              = 0,
    val error:            String?          = null,
) {
    val progress: Float
        get() = if (durationMs > 0L) positionMs.toFloat() / durationMs else 0f

    val hasNext: Boolean
        get() = repeatMode == MrRepeatMode.All || repeatMode == MrRepeatMode.One
            || currentIndex < queue.lastIndex

    val hasPrev: Boolean
        get() = positionMs > 3_000L || currentIndex > 0
}
