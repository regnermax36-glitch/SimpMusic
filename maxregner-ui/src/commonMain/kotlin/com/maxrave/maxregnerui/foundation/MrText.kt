package com.maxrave.maxregnerui.foundation

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.maxrave.maxregnerui.MrTheme

@Composable
fun MrText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MrTheme.typography.bodyMedium,
    color: Color = MrTheme.colors.onSurface,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = if (color == Color.Unspecified) style.color else color),
        maxLines = maxLines,
        overflow = overflow,
    )
}

@Composable
fun MrTitle(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) = MrText(
    text = text,
    modifier = modifier,
    style = MrTheme.typography.titleMedium,
    color = MrTheme.colors.onSurface,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
)

@Composable
fun MrSubtitle(
    text: String,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
) = MrText(
    text = text,
    modifier = modifier,
    style = MrTheme.typography.bodySmall,
    color = MrTheme.colors.onSurfaceMuted,
    maxLines = maxLines,
    overflow = TextOverflow.Ellipsis,
)

@Composable
fun MrLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MrTheme.colors.onSurfaceMuted,
) = MrText(
    text = text,
    modifier = modifier,
    style = MrTheme.typography.labelSmall,
    color = color,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
)
