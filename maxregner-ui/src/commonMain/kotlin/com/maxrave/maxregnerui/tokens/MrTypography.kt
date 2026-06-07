package com.maxrave.maxregnerui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
data class MrTypography(
    val fontFamily: FontFamily = FontFamily.Default,

    val displayLarge:   TextStyle = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold,     letterSpacing = (-0.5).sp),
    val displayMedium:  TextStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold,     letterSpacing = (-0.25).sp),
    val displaySmall:   TextStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),

    val headlineLarge:  TextStyle = TextStyle(fontSize = 23.sp, fontWeight = FontWeight.Bold),
    val headlineMedium: TextStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    val headlineSmall:  TextStyle = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.SemiBold),

    val titleLarge:     TextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    val titleMedium:    TextStyle = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
    val titleSmall:     TextStyle = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.SemiBold),

    val bodyLarge:      TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Normal),
    val bodyMedium:     TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    val bodySmall:      TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),

    val labelLarge:     TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    val labelMedium:    TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
    val labelSmall:     TextStyle = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Medium,   letterSpacing = 0.4.sp),
)

val LocalMrTypography = staticCompositionLocalOf { MrTypography() }
