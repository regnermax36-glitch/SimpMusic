package com.maxrave.maxregnerui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme

@Composable
fun MrButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MrTheme.colors.primary,
    contentColor: Color = MrTheme.colors.onPrimary,
    padding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
    cornerRadius: Dp = MrTheme.shapes.button,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press   -> scale.animateTo(0.94f, tween(80))
                is PressInteraction.Release,
                is PressInteraction.Cancel  -> scale.animateTo(1f,    tween(120))
            }
        }
    }

    Box(
        modifier = modifier
            .scale(scale.value)
            .clip(RoundedCornerShape(cornerRadius))
            .background(if (enabled) containerColor else containerColor.copy(alpha = 0.38f))
            .clickable(
                interactionSource = interactionSource,
                indication = MrRipple.create(contentColor.copy(alpha = 0.2f)),
                enabled = enabled,
                onClick = onClick,
            )
            .padding(padding),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = text,
            style = MrTheme.typography.labelLarge.copy(
                color = if (enabled) contentColor else contentColor.copy(alpha = 0.38f),
            ),
        )
    }
}

@Composable
fun MrIconButton(
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tint: Color = MrTheme.colors.onSurface,
    containerColor: Color = Color.Transparent,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    icon: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press   -> scale.animateTo(0.84f, tween(80))
                is PressInteraction.Release,
                is PressInteraction.Cancel  -> scale.animateTo(1f,    tween(120))
            }
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(scale.value)
            .clip(RoundedCornerShape(MrTheme.shapes.full))
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = MrRipple.create(tint.copy(alpha = 0.2f)),
                enabled = enabled,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.size(iconSize)) { icon() }
    }
}

@Composable
fun MrPlayPauseButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    iconSize: Dp = 32.dp,
    containerColor: Color = MrTheme.colors.primary,
    iconTint: Color = MrTheme.colors.onPrimary,
    playIcon: @Composable () -> Unit,
    pauseIcon: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = remember { Animatable(1f) }

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press   -> scale.animateTo(0.88f, tween(80))
                is PressInteraction.Release,
                is PressInteraction.Cancel  -> scale.animateTo(1f,    tween(130))
            }
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .scale(scale.value)
            .clip(RoundedCornerShape(MrTheme.shapes.full))
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = MrRipple.create(iconTint.copy(alpha = 0.2f)),
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Box(modifier = Modifier.size(iconSize)) {
            if (isPlaying) pauseIcon() else playIcon()
        }
    }
}
