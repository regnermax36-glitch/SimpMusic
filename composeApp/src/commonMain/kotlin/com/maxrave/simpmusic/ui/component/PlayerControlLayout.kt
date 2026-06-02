package com.maxrave.simpmusic.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxrave.domain.mediaservice.handler.RepeatState
import com.maxrave.simpmusic.ui.theme.*
import org.jetbrains.compose.resources.painterResource
import simpmusic.composeapp.generated.resources.*

// ─── Aurora Player Controls ────────────────────────────────────────────────────

@Composable
fun PlayerControlLayout(
    isPlaying: Boolean,
    repeatState: RepeatState,
    shuffleState: Boolean,
    currentPosition: Long,
    totalDuration: Long,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onRepeat: () -> Unit,
    onShuffle: () -> Unit,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // ── Progress bar ──────────────────────────────────────────────────────
        AuroraProgressBar(
            position  = currentPosition,
            duration  = totalDuration,
            onSeek    = onSeek,
            modifier  = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(24.dp))

        // ── Control row ───────────────────────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Shuffle
            ControlIconButton(
                iconRes   = Res.drawable.baseline_shuffle_24,
                isActive  = shuffleState,
                onClick   = onShuffle,
                size      = 44.dp,
                iconSize  = 22.dp,
            )

            // Previous
            ControlIconButton(
                iconRes   = Res.drawable.baseline_skip_previous_24,
                onClick   = onPrevious,
                size      = 52.dp,
                iconSize  = 28.dp,
            )

            // Play / Pause — primary large button
            PlayPauseGlowButton(
                isPlaying = isPlaying,
                onClick   = onPlayPause,
            )

            // Next
            ControlIconButton(
                iconRes   = Res.drawable.baseline_skip_next_24,
                onClick   = onNext,
                size      = 52.dp,
                iconSize  = 28.dp,
            )

            // Repeat
            ControlIconButton(
                iconRes   = when (repeatState) {
                    RepeatState.ONE  -> Res.drawable.baseline_repeat_one_24
                    else             -> Res.drawable.baseline_repeat_24
                },
                isActive  = repeatState != RepeatState.NONE,
                onClick   = onRepeat,
                size      = 44.dp,
                iconSize  = 22.dp,
            )
        }
    }
}

// ─── Aurora Progress Bar ───────────────────────────────────────────────────────

@Composable
fun AuroraProgressBar(
    position: Long,
    duration: Long,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = if (duration > 0) (position.toFloat() / duration.toFloat()).coerceIn(0f, 1f) else 0f

    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth().height(32.dp), contentAlignment = Alignment.Center) {
            // Track background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(SliderTrack)
            )
            // Progress fill (gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .align(Alignment.CenterStart)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(AuroraBrush)
            )
            // Thumb
            Box(
                modifier = Modifier
                    .offset(x = ((progress - 0.01f) * 300).dp.coerceAtLeast(0.dp))  // approximation
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(AuroraBrush)
                    .align(Alignment.CenterStart)
            )
            // Transparent full-width seek target
            Slider(
                value         = progress,
                onValueChange = onSeek,
                colors        = SliderDefaults.colors(
                    thumbColor             = Color.Transparent,
                    activeTrackColor       = Color.Transparent,
                    inactiveTrackColor     = Color.Transparent,
                    activeTickColor        = Color.Transparent,
                    inactiveTickColor      = Color.Transparent,
                ),
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text  = formatMillis(position),
                style = typo().labelSmall.copy(color = TextTertiary, fontSize = 11.sp),
            )
            Text(
                text  = formatMillis(duration),
                style = typo().labelSmall.copy(color = TextTertiary, fontSize = 11.sp),
            )
        }
    }
}

private fun formatMillis(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

// ─── Glowing Play/Pause button ────────────────────────────────────────────────

@Composable
fun PlayPauseGlowButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp = 72.dp,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue  = if (isPressed) 0.93f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "playBtnScale",
    )

    // Pulsing glow when playing
    val infiniteTransition = rememberInfiniteTransition(label = "glowPulse")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue   = if (isPlaying) 0.3f else 0f,
        targetValue    = if (isPlaying) 0.6f else 0f,
        animationSpec  = infiniteRepeatable(
            animation  = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "glowAlpha",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size + 20.dp),
    ) {
        // Outer glow halo
        Box(
            modifier = Modifier
                .size(size + 16.dp)
                .clip(CircleShape)
                .background(Aurora1.copy(alpha = glowAlpha))
        )
        // Main button
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(size)
                .scale(scale)
                .shadow(elevation = 12.dp, shape = CircleShape, ambientColor = Aurora1, spotColor = Aurora1)
                .clip(CircleShape)
                .background(AuroraBrush)
                .clickable(
                    interactionSource = interactionSource,
                    indication        = null,
                    onClick           = onClick,
                )
        ) {
            Icon(
                painter = painterResource(
                    if (isPlaying) Res.drawable.baseline_pause_24 else Res.drawable.baseline_play_arrow_24
                ),
                contentDescription = if (isPlaying) "Pause" else "Play",
                tint   = Color.White,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}

// ─── Generic control icon button ──────────────────────────────────────────────

@Composable
fun ControlIconButton(
    iconRes: org.jetbrains.compose.resources.DrawableResource,
    onClick: () -> Unit,
    size: androidx.compose.ui.unit.Dp = 44.dp,
    iconSize: androidx.compose.ui.unit.Dp = 22.dp,
    isActive: Boolean = false,
    tint: Color = if (isActive) Aurora1 else TextSecondary,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue  = if (isPressed) 0.88f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessHigh),
        label = "ctrlScale",
    )
    val resolvedTint by animateColorAsState(tint, label = "ctrlTint")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(size)
            .scale(scale)
            .clip(CircleShape)
            .then(if (isActive) Modifier.background(Aurora1.copy(alpha = 0.12f)) else Modifier)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
    ) {
        Icon(
            painter            = painterResource(iconRes),
            contentDescription = null,
            tint               = resolvedTint,
            modifier           = Modifier.size(iconSize),
        )
    }
}
