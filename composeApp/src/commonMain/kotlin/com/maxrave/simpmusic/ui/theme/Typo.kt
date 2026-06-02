package com.maxrave.simpmusic.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import simpmusic.composeapp.generated.resources.Res
import simpmusic.composeapp.generated.resources.poppins_medium

// ─── Typography — Aurora Design System ────────────────────────────────────────
// Poppins for all text; tighter letter spacing for modern feel

@Composable
fun fontFamily(): FontFamily =
    FontFamily(
        Font(Res.font.poppins_medium, FontWeight.Normal,    FontStyle.Normal),
        Font(Res.font.poppins_medium, FontWeight.SemiBold,  FontStyle.Normal),
        Font(Res.font.poppins_medium, FontWeight.Bold,      FontStyle.Normal),
    )

@Composable
fun typo(): Typography {
    val ff = fontFamily()
    return Typography(
        // ── Display: hero text (artist name banners, big stats) ──────────────
        displayLarge = TextStyle(
            fontSize      = 32.sp,
            fontWeight    = FontWeight.Bold,
            fontFamily    = ff,
            color         = TextPrimary,
            letterSpacing = (-0.5).sp,
            lineHeight    = 38.sp,
        ),
        displayMedium = TextStyle(
            fontSize      = 26.sp,
            fontWeight    = FontWeight.Bold,
            fontFamily    = ff,
            color         = TextPrimary,
            letterSpacing = (-0.3).sp,
            lineHeight    = 32.sp,
        ),
        displaySmall = TextStyle(
            fontSize      = 22.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextPrimary,
            letterSpacing = (-0.2).sp,
            lineHeight    = 28.sp,
        ),

        // ── Headlines: section headers, screen titles ─────────────────────────
        headlineLarge = TextStyle(
            fontSize      = 24.sp,
            fontWeight    = FontWeight.Bold,
            fontFamily    = ff,
            color         = TextPrimary,
            letterSpacing = (-0.2).sp,
            lineHeight    = 30.sp,
        ),
        headlineMedium = TextStyle(
            fontSize      = 20.sp,
            fontWeight    = FontWeight.Bold,
            fontFamily    = ff,
            color         = TextPrimary,
            letterSpacing = (-0.1).sp,
            lineHeight    = 26.sp,
        ),
        headlineSmall = TextStyle(
            fontSize      = 17.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextPrimary,
            lineHeight    = 22.sp,
        ),

        // ── Titles: song/album/playlist names ─────────────────────────────────
        titleLarge = TextStyle(
            fontSize      = 18.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextPrimary,
            lineHeight    = 24.sp,
        ),
        titleMedium = TextStyle(
            fontSize      = 15.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextPrimary,
            lineHeight    = 20.sp,
        ),
        titleSmall = TextStyle(
            fontSize      = 13.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextPrimary,
            lineHeight    = 18.sp,
        ),

        // ── Body: descriptions, subtitles, artist names ───────────────────────
        bodyLarge = TextStyle(
            fontSize      = 16.sp,
            fontWeight    = FontWeight.Normal,
            fontFamily    = ff,
            color         = TextSecondary,
            lineHeight    = 22.sp,
        ),
        bodyMedium = TextStyle(
            fontSize      = 14.sp,
            fontWeight    = FontWeight.Normal,
            fontFamily    = ff,
            color         = TextSecondary,
            lineHeight    = 20.sp,
        ),
        bodySmall = TextStyle(
            fontSize      = 12.sp,
            fontWeight    = FontWeight.Normal,
            fontFamily    = ff,
            color         = TextTertiary,
            lineHeight    = 16.sp,
        ),

        // ── Labels: chips, badges, captions, tab labels ───────────────────────
        labelLarge = TextStyle(
            fontSize      = 14.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextPrimary,
            letterSpacing = 0.1.sp,
        ),
        labelMedium = TextStyle(
            fontSize      = 12.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextSecondary,
            letterSpacing = 0.1.sp,
        ),
        labelSmall = TextStyle(
            fontSize      = 10.sp,
            fontWeight    = FontWeight.SemiBold,
            fontFamily    = ff,
            color         = TextTertiary,
            letterSpacing = 0.2.sp,
        ),
    )
}
