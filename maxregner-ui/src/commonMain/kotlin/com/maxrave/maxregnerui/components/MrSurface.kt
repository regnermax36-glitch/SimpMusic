package com.maxrave.maxregnerui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.maxrave.maxregnerui.MrTheme

/**
 * Base building block. Clips to rounded rect and optionally handles clicks.
 * Built on Compose foundation only — no Material Surface.
 */
@Composable
fun MrSurface(
    modifier: Modifier = Modifier,
    color: Color = MrTheme.colors.surfaceVariant,
    cornerRadius: Dp = MrTheme.shapes.card,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val shape = RoundedCornerShape(cornerRadius)
    val base = modifier.clip(shape).background(color)
    Box(
        modifier = if (onClick != null) {
            val source = remember { MutableInteractionSource() }
            base.clickable(interactionSource = source, indication = MrRipple.create(), onClick = onClick)
        } else base,
        content = content,
    )
}
