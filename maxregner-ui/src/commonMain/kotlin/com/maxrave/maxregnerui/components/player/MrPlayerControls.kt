package com.maxrave.maxregnerui.components.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme
import com.maxrave.maxregnerui.components.MrIconButton
import com.maxrave.maxregnerui.components.MrPlayPauseButton
import com.maxrave.maxregnerui.foundation.MrSubtitle
import com.maxrave.maxregnerui.foundation.MrTitle

enum class MrRepeatMode { Off, All, One }

/**
 * Full-screen player transport controls.
 * Icons are passed as composable slots so the caller decides what to render
 * (vector drawable, painter, etc.) — maxregner-ui is icon-system agnostic.
 */
@Composable
fun MrPlayerControls(
    title: String,
    artist: String,
    isPlaying: Boolean,
    progress: Float,
    repeatMode: MrRepeatMode,
    isShuffleOn: Boolean,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onSeek: (Float) -> Unit,
    onSeekFinished: () -> Unit,
    onRepeatToggle: () -> Unit,
    onShuffleToggle: () -> Unit,
    modifier: Modifier = Modifier,
    // Icon slots — no Material Icons dependency inside this lib
    shuffleIcon: @Composable () -> Unit,
    previousIcon: @Composable () -> Unit,
    nextIcon: @Composable () -> Unit,
    playIcon: @Composable () -> Unit,
    pauseIcon: @Composable () -> Unit,
    repeatIcon: @Composable () -> Unit,
    repeatOneIcon: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MrTheme.spacing.screen),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Track title + artist
        MrTitle(text = title, maxLines = 1)
        Spacer(Modifier.height(4.dp))
        MrSubtitle(text = artist, maxLines = 1)

        Spacer(Modifier.height(MrTheme.spacing.xxl))

        // Seek bar
        MrProgressSlider(
            value = progress,
            onValueChange = onSeek,
            onValueChangeFinished = onSeekFinished,
        )

        Spacer(Modifier.height(MrTheme.spacing.xl))

        // Transport row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MrIconButton(
                contentDescription = "Shuffle",
                onClick = onShuffleToggle,
                tint = if (isShuffleOn) MrTheme.colors.primary else MrTheme.colors.onSurfaceMuted,
                size = 44.dp, iconSize = 22.dp,
                icon = shuffleIcon,
            )
            MrIconButton(
                contentDescription = "Previous",
                onClick = onPrevious,
                size = 52.dp, iconSize = 28.dp,
                icon = previousIcon,
            )
            MrPlayPauseButton(
                isPlaying = isPlaying,
                onClick = onPlayPause,
                size = 68.dp, iconSize = 36.dp,
                playIcon = playIcon,
                pauseIcon = pauseIcon,
            )
            MrIconButton(
                contentDescription = "Next",
                onClick = onNext,
                size = 52.dp, iconSize = 28.dp,
                icon = nextIcon,
            )
            MrIconButton(
                contentDescription = "Repeat",
                onClick = onRepeatToggle,
                tint = if (repeatMode != MrRepeatMode.Off) MrTheme.colors.primary else MrTheme.colors.onSurfaceMuted,
                size = 44.dp, iconSize = 22.dp,
                icon = if (repeatMode == MrRepeatMode.One) repeatOneIcon else repeatIcon,
            )
        }
    }
}
