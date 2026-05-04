package com.maxrave.simpmusic.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AlienEqualizer(
    modifier: Modifier = Modifier,
    barCount: Int = 12,
    color: Color,
    isPlaying: Boolean = true
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        for (i in 0 until barCount) {
            AlienEqualizerBar(index = i, isPlaying = isPlaying, color = color)
        }
    }
}

@Composable
fun AlienEqualizerBar(index: Int, isPlaying: Boolean, color: Color) {
    val infiniteTransition = rememberInfiniteTransition(label = "equalizer_$index")

    // Remember the duration so it's only generated once per bar
    val duration = remember { (300..1000).random() }

    val heightMultiplier by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = if (isPlaying) 1f else 0.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = duration, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "barHeight_$index"
    )

    // Random base height factor to make bars look natural
    val baseFactor = remember { 0.5f + (index % 3) * 0.2f }

    Box(
        modifier = Modifier
            .width(10.dp)
            .fillMaxHeight(heightMultiplier * baseFactor)
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(color.copy(alpha = 0.8f))
    )
}
