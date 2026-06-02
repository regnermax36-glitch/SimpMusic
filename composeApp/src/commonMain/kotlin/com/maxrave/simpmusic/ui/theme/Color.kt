package com.maxrave.simpmusic.ui.theme

import androidx.compose.ui.graphics.Color

// ─── Aurora Design System — SimpMusic v2 ──────────────────────────────────────
// Primary palette: deep space purple → electric violet → neon coral accent
// Designed for AMOLED-first, high-contrast, vivid music experience

// ── Core Brand Colors ──────────────────────────────────────────────────────────
val Aurora1 = Color(0xFF6C63FF)          // Electric violet (primary)
val Aurora2 = Color(0xFF9C59FF)          // Purple mid
val Aurora3 = Color(0xFFFF6B9D)          // Neon coral (accent)
val Aurora4 = Color(0xFF00D4FF)          // Cyan highlight
val Aurora5 = Color(0xFF1AFF9C)          // Neon mint (success)

// ── Background Layers (true AMOLED) ───────────────────────────────────────────
val SpaceBlack   = Color(0xFF000000)     // True AMOLED black
val DeepVoid     = Color(0xFF08080F)     // Near-black with blue tint
val DarkCavity   = Color(0xFF0F0F1A)     // Card background
val ElevatedSurface = Color(0xFF161625) // Elevated surfaces
val ModalSurface = Color(0xFF1C1C30)    // Bottom sheets / dialogs
val BorderSubtle = Color(0xFF2A2A45)    // Subtle borders
val BorderMed    = Color(0xFF3D3D65)    // Medium borders

// ── Text Colors ───────────────────────────────────────────────────────────────
val TextPrimary   = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFB0AECB)   // Muted purple-white
val TextTertiary  = Color(0xFF6B6A8A)   // Dim text
val TextDisabled  = Color(0xFF3D3D5C)

// ── Semantic Colors ───────────────────────────────────────────────────────────
val Success  = Color(0xFF1AFF9C)
val Warning  = Color(0xFFFFB347)
val Error    = Color(0xFFFF4D6D)
val Info     = Color(0xFF00D4FF)

// ── Gradient Stops ────────────────────────────────────────────────────────────
val GradientStart  = Color(0xFF6C63FF)
val GradientMid    = Color(0xFF9C59FF)
val GradientEnd    = Color(0xFFFF6B9D)

// ── Glassmorphism / Overlay ───────────────────────────────────────────────────
val GlassLight     = Color(0x1AFFFFFF)   // 10% white
val GlassMid       = Color(0x33FFFFFF)   // 20% white
val GlassStrong    = Color(0x4DFFFFFF)   // 30% white
val OverlayDark    = Color(0xB3000000)   // 70% black
val OverlayMed     = Color(0x80000000)   // 50% black

// ── Player-specific ───────────────────────────────────────────────────────────
val PlayerGlow     = Color(0x406C63FF)   // Violet glow
val PlayerAccent   = Aurora3
val SliderTrack    = Color(0xFF2A2A45)
val SliderProgress = Aurora1

// ── Material3 theme tokens ────────────────────────────────────────────────────
val md_theme_dark_primary           = Aurora1
val md_theme_dark_onPrimary         = TextPrimary
val md_theme_dark_primaryContainer  = Color(0xFF1E1A4F)
val md_theme_dark_onPrimaryContainer = Color(0xFFCFCBFF)
val md_theme_dark_secondary         = Aurora2
val md_theme_dark_onSecondary       = TextPrimary
val md_theme_dark_secondaryContainer = Color(0xFF2A1A4F)
val md_theme_dark_onSecondaryContainer = Color(0xFFDDCBFF)
val md_theme_dark_tertiary          = Aurora3
val md_theme_dark_onTertiary        = TextPrimary
val md_theme_dark_tertiaryContainer = Color(0xFF4F1A2D)
val md_theme_dark_onTertiaryContainer = Color(0xFFFFCBDB)
val md_theme_dark_error             = Error
val md_theme_dark_errorContainer    = Color(0xFF4F0010)
val md_theme_dark_onError           = TextPrimary
val md_theme_dark_onErrorContainer  = Color(0xFFFFB3C1)
val md_theme_dark_background        = SpaceBlack
val md_theme_dark_onBackground      = TextPrimary
val md_theme_dark_surface           = DarkCavity
val md_theme_dark_onSurface         = TextPrimary
val md_theme_dark_surfaceVariant    = ElevatedSurface
val md_theme_dark_onSurfaceVariant  = TextSecondary
val md_theme_dark_outline           = BorderMed
val md_theme_dark_inverseOnSurface  = DeepVoid
val md_theme_dark_inverseSurface    = TextPrimary
val md_theme_dark_inversePrimary    = Color(0xFF3A2FA0)
val md_theme_dark_shadow            = SpaceBlack
val md_theme_dark_surfaceTint       = Aurora1
val md_theme_dark_outlineVariant    = BorderSubtle
val md_theme_dark_scrim             = OverlayDark

// ── Legacy compat aliases ─────────────────────────────────────────────────────
val colorPrimaryDark       = Color(0x19000000)
val back_button_color      = Color(0x33FFFFFF)
val checkedFilterColor     = Aurora1.copy(alpha = 0.25f)
val shimmerBackground      = Color(0x7E0F0F1A)
val shimmerLine            = ElevatedSurface
val test                   = Color(0x00FFFFFF)
val overlay                = OverlayMed
val blackMoreOverlay        = OverlayDark
val seed                   = Aurora1
val bottomBarSeedDark      = Aurora1
val customGray             = Color(0x40ECECEC)
val customDarkGray         = Color(0x40161625)
val white                  = Color(0xFFFFFFFF)
val transparent            = Color(0x00000000)
