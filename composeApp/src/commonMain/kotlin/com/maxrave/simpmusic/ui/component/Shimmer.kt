package com.maxrave.simpmusic.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maxrave.simpmusic.ui.theme.*

// ─── Aurora Shimmer System ─────────────────────────────────────────────────────

@Composable
fun shimmerBrush(showShimmer: Boolean = true, targetValue: Float = 1000f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            ElevatedSurface,
            ModalSurface,
            ElevatedSurface,
            DarkCavity,
        )
        val transition = rememberInfiniteTransition(label = "shimmer")
        val translateAnimation by transition.animateFloat(
            initialValue   = 0f,
            targetValue    = targetValue,
            animationSpec  = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing)),
            label          = "shimmerAnim",
        )
        Brush.linearGradient(
            colors      = shimmerColors,
            start       = Offset.Zero,
            end         = Offset(translateAnimation, translateAnimation),
        )
    } else {
        Brush.linearGradient(listOf(ElevatedSurface, ElevatedSurface))
    }
}

/** Rectangular shimmer placeholder */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(shimmerBrush())
    )
}

/** Circle shimmer placeholder (for avatars) */
@Composable
fun ShimmerCircle(size: Dp = 48.dp) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(shimmerBrush())
    )
}

/** Song item row shimmer */
@Composable
fun SongItemShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
    ) {
        ShimmerBox(Modifier.size(56.dp), cornerRadius = 10.dp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            ShimmerBox(Modifier.fillMaxWidth(0.7f).height(14.dp))
            ShimmerBox(Modifier.fillMaxWidth(0.45f).height(11.dp))
        }
        ShimmerBox(Modifier.size(36.dp), cornerRadius = 18.dp)
    }
}

/** Horizontal card shimmer (for home shelf items) */
@Composable
fun HorizontalCardShimmer(
    modifier: Modifier = Modifier,
    width: Dp = 140.dp,
    height: Dp = 140.dp,
) {
    Column(
        modifier = modifier.width(width),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        ShimmerBox(Modifier.size(width, height), cornerRadius = 12.dp)
        ShimmerBox(Modifier.fillMaxWidth(0.8f).height(12.dp))
        ShimmerBox(Modifier.fillMaxWidth(0.5f).height(10.dp))
    }
}

/** List of song shimmers for loading state */
@Composable
fun SongListShimmer(count: Int = 6) {
    Column {
        repeat(count) {
            SongItemShimmer()
        }
    }
}
