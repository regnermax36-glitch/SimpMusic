package com.maxrave.maxregnerui.components.player

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme
import com.maxrave.maxregnerui.components.MrIconButton
import com.maxrave.maxregnerui.components.MrRipple
import com.maxrave.maxregnerui.foundation.MrSubtitle
import com.maxrave.maxregnerui.foundation.MrTitle

@Composable
fun MrMiniPlayer(
    title: String,
    artist: String,
    isPlaying: Boolean,
    progress: Float,
    onClick: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnail: @Composable (() -> Unit)? = null,
    playIcon: @Composable () -> Unit,
    pauseIcon: @Composable () -> Unit,
    nextIcon: @Composable () -> Unit,
) {
    val source = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(MrTheme.shapes.miniPlayer))
            .background(MrTheme.colors.surfaceVariant)
            .clickable(
                interactionSource = source,
                indication = MrRipple.create(MrTheme.colors.onSurface.copy(alpha = 0.1f)),
                onClick = onClick,
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MrTheme.spacing.miniPlayerH)
                .padding(horizontal = MrTheme.spacing.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (thumbnail != null) {
                Box(
                    Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(MrTheme.shapes.thumbnail)),
                ) { thumbnail() }
                Spacer(Modifier.width(MrTheme.spacing.md))
            }
            Column(modifier = Modifier.weight(1f)) {
                MrTitle(text = title, maxLines = 1)
                Spacer(Modifier.height(2.dp))
                MrSubtitle(text = artist, maxLines = 1)
            }
            MrIconButton(
                contentDescription = if (isPlaying) "Pause" else "Play",
                onClick = onPlayPause,
                tint = MrTheme.colors.onSurface,
                size = 40.dp, iconSize = 22.dp,
                icon = if (isPlaying) pauseIcon else playIcon,
            )
            Spacer(Modifier.width(4.dp))
            MrIconButton(
                contentDescription = "Next",
                onClick = onNext,
                tint = MrTheme.colors.onSurface,
                size = 40.dp, iconSize = 22.dp,
                icon = nextIcon,
            )
        }
        // Progress strip
        Box(
            Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(MrTheme.colors.progressTrack),
        ) {
            Box(
                Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(2.dp)
                    .background(MrTheme.colors.progressFill),
            )
        }
    }
}
