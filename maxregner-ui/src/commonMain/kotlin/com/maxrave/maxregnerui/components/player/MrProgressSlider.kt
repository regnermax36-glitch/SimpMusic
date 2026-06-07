package com.maxrave.maxregnerui.components.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme
import kotlin.math.roundToInt

/**
 * Custom seek bar — zero Material dependency.
 * Tap anywhere or drag the thumb to seek.
 * Thumb animates size on press.
 */
@Composable
fun MrProgressSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    onValueChangeFinished: (() -> Unit)? = null,
    trackHeight: Dp = 4.dp,
    thumbSize: Dp = 14.dp,
    thumbActiveSize: Dp = 20.dp,
    trackColor: Color = MrTheme.colors.progressTrack,
    progressColor: Color = MrTheme.colors.progressFill,
    thumbColor: Color = MrTheme.colors.thumbColor,
) {
    val density = LocalDensity.current
    var trackWidthPx by remember { mutableFloatStateOf(1f) }
    var dragging by remember { mutableStateOf(false) }

    val animThumbSizePx = remember { Animatable(with(density) { thumbSize.toPx() }) }
    LaunchedEffect(dragging) {
        val target = with(density) { if (dragging) thumbActiveSize.toPx() else thumbSize.toPx() }
        animThumbSizePx.animateTo(target, tween(120))
    }

    val clamped = value.coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbActiveSize)
            .onSizeChanged { trackWidthPx = it.width.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    onValueChange((offset.x / trackWidthPx).coerceIn(0f, 1f))
                    onValueChangeFinished?.invoke()
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragStart  = { dragging = true },
                    onDragEnd    = { dragging = false; onValueChangeFinished?.invoke() },
                    onDragCancel = { dragging = false },
                    onHorizontalDrag = { change, _ ->
                        onValueChange((change.position.x / trackWidthPx).coerceIn(0f, 1f))
                    },
                )
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        // Background track
        Box(
            Modifier
                .fillMaxWidth()
                .height(trackHeight)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(999.dp))
                .background(trackColor),
        )
        // Progress fill
        Box(
            Modifier
                .fillMaxWidth(clamped.coerceAtLeast(0.01f))
                .height(trackHeight)
                .align(Alignment.CenterStart)
                .clip(RoundedCornerShape(999.dp))
                .background(progressColor),
        )
        // Thumb
        val thumbDp = with(density) { animThumbSizePx.value.toDp() }
        Box(
            Modifier
                .offset {
                    IntOffset(
                        x = ((clamped * trackWidthPx) - animThumbSizePx.value / 2).roundToInt(),
                        y = 0,
                    )
                }
                .size(thumbDp)
                .clip(CircleShape)
                .background(thumbColor),
        )
    }
}
