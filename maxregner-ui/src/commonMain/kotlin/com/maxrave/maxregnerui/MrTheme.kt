package com.maxrave.maxregnerui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import com.maxrave.maxregnerui.tokens.LocalMrColors
import com.maxrave.maxregnerui.tokens.LocalMrShapes
import com.maxrave.maxregnerui.tokens.LocalMrSpacing
import com.maxrave.maxregnerui.tokens.LocalMrTypography
import com.maxrave.maxregnerui.tokens.MrColors
import com.maxrave.maxregnerui.tokens.MrShapes
import com.maxrave.maxregnerui.tokens.MrSpacing
import com.maxrave.maxregnerui.tokens.MrTypography

@Composable
fun MrTheme(
    colors: MrColors = MrColors(),
    typography: MrTypography = MrTypography(),
    spacing: MrSpacing = MrSpacing(),
    shapes: MrShapes = MrShapes(),
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalMrColors provides colors,
        LocalMrTypography provides typography,
        LocalMrSpacing provides spacing,
        LocalMrShapes provides shapes,
        content = content,
    )
}

object MrTheme {
    val colors: MrColors
        @Composable @ReadOnlyComposable get() = LocalMrColors.current
    val typography: MrTypography
        @Composable @ReadOnlyComposable get() = LocalMrTypography.current
    val spacing: MrSpacing
        @Composable @ReadOnlyComposable get() = LocalMrSpacing.current
    val shapes: MrShapes
        @Composable @ReadOnlyComposable get() = LocalMrShapes.current
}
