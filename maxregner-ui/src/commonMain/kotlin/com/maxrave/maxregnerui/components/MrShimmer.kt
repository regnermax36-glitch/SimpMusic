package com.maxrave.maxregnerui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme

@Composable
fun rememberMrShimmerBrush(): Brush {
    val transition = rememberInfiniteTransition(label = "mr_shimmer")
    val x by transition.animateFloat(
        initialValue = -800f,
        targetValue  = 800f,
        animationSpec = infiniteRepeatable(
            animation  = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "mr_shimmer_x",
    )
    return Brush.linearGradient(
        colors = listOf(
            MrTheme.colors.shimmerBase,
            MrTheme.colors.shimmerHighlight,
            MrTheme.colors.shimmerBase,
        ),
        start = Offset(x, 0f),
        end   = Offset(x + 500f, 0f),
    )
}

@Composable
fun MrShimmerBox(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = MrTheme.shapes.sm,
    content: @Composable BoxScope.() -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(rememberMrShimmerBrush()),
        content = content,
    )
}

@Composable
fun MrTrackItemShimmer(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MrTheme.spacing.listItemH, vertical = MrTheme.spacing.listItemV),
    ) {
        MrShimmerBox(
            modifier = Modifier.size(MrTheme.spacing.thumbMd),
            cornerRadius = MrTheme.shapes.thumbnail,
        )
        Spacer(Modifier.width(MrTheme.spacing.md))
        Column(modifier = Modifier.weight(1f)) {
            MrShimmerBox(Modifier.fillMaxWidth(0.68f).height(13.dp))
            Spacer(Modifier.height(6.dp))
            MrShimmerBox(Modifier.fillMaxWidth(0.44f).height(11.dp))
        }
    }
}

@Composable
fun MrCardShimmer(imageSize: Dp = 140.dp, modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(MrTheme.spacing.xs)) {
        MrShimmerBox(Modifier.size(imageSize), cornerRadius = MrTheme.shapes.thumbnail)
        Spacer(Modifier.height(6.dp))
        MrShimmerBox(Modifier.width(imageSize * 0.8f).height(13.dp))
        Spacer(Modifier.height(4.dp))
        MrShimmerBox(Modifier.width(imageSize * 0.55f).height(11.dp))
    }
}
