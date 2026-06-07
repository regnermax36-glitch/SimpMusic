package com.maxrave.maxregneplayer

import kotlinx.serialization.Serializable

/**
 * Platform-agnostic track model for [MrPlayer].
 *
 * @param id        Unique stable ID (e.g. YouTube video ID)
 * @param streamUrl Direct streamable audio URL (HTTP/HTTPS)
 * @param title     Display title
 * @param artist    Artist/channel name
 * @param artUrl    Album art URL
 * @param durationMs Known duration, or null when unknown until probed
 * @param isVideo   When true, crossfade is skipped (audio-only feature)
 * @param mimeHint  Optional MIME type hint ("audio/mp4", "audio/webm", etc.)
 */
@Serializable
data class MrTrack(
    val id: String,
    val streamUrl: String,
    val title: String,
    val artist: String,
    val artUrl: String? = null,
    val durationMs: Long? = null,
    val isVideo: Boolean = false,
    val mimeHint: String? = null,
    val extras: Map<String, String> = emptyMap(),
)
