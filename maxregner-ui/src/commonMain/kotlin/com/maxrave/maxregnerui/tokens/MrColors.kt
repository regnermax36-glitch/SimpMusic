package com.maxrave.maxregnerui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MrColors(
    val primary: Color          = Color(0xFFB2C5FF),
    val onPrimary: Color        = Color(0xFF1E438F),
    val primaryMuted: Color     = Color(0x26B2C5FF),

    val secondary: Color        = Color(0xFFC0C6DD),
    val onSecondary: Color      = Color(0xFF2A3042),
    val secondaryMuted: Color   = Color(0x26C0C6DD),

    val accent: Color           = Color(0xFF53A7D0),
    val accentMuted: Color      = Color(0x2653A7D0),

    val surface: Color          = Color(0xFF0A0A0A),
    val surfaceElevated: Color  = Color(0xFF141414),
    val surfaceVariant: Color   = Color(0xFF1E1E1E),
    val surfaceOverlay: Color   = Color(0x33FFFFFF),

    val onSurface: Color        = Color(0xFFE4E2E6),
    val onSurfaceMuted: Color   = Color(0xFFA8A8A8),
    val onSurfaceDim: Color     = Color(0xFF6B6B6B),

    val outline: Color          = Color(0xFF3A3A3A),
    val outlineVariant: Color   = Color(0xFF2A2A2A),

    val error: Color            = Color(0xFFFFB4AB),
    val onError: Color          = Color(0xFF690005),

    val scrim: Color            = Color(0xBF000000),
    val transparent: Color      = Color(0x00000000),

    // Player-specific
    val progressTrack: Color    = Color(0x33FFFFFF),
    val progressFill: Color     = Color(0xFFB2C5FF),
    val thumbColor: Color       = Color(0xFFFFFFFF),

    // Shimmer
    val shimmerBase: Color      = Color(0xFF1A1A1A),
    val shimmerHighlight: Color = Color(0xFF2E2E2E),
)

val LocalMrColors = staticCompositionLocalOf { MrColors() }
