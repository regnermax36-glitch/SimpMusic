package com.maxrave.maxregnerui.components.list

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme
import com.maxrave.maxregnerui.components.MrRipple
import com.maxrave.maxregnerui.foundation.MrSubtitle
import com.maxrave.maxregnerui.foundation.MrTitle

/**
 * Row item for a song/track. Icon slots for thumbnail and trailing action are
 * composable lambdas so the app decides how to load images.
 */
@Composable
fun MrTrackItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnailSize: Dp = MrTheme.spacing.thumbMd,
    isActive: Boolean = false,
    onMoreClick: (() -> Unit)? = null,
    thumbnail: @Composable (() -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
) {
    val source = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = source,
                indication = MrRipple.create(MrTheme.colors.onSurface.copy(alpha = 0.08f)),
                onClick = onClick,
            )
            .padding(horizontal = MrTheme.spacing.listItemH, vertical = MrTheme.spacing.listItemV),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Active indicator
        Box(
            Modifier
                .width(3.dp)
                .height(thumbnailSize)
                .clip(RoundedCornerShape(999.dp))
                .background(if (isActive) MrTheme.colors.primary else Color.Transparent),
        )
        if (isActive) Spacer(Modifier.width(MrTheme.spacing.sm))

        if (thumbnail != null) {
            Box(
                Modifier.size(thumbnailSize)
                    .clip(RoundedCornerShape(MrTheme.shapes.thumbnail)),
            ) { thumbnail() }
            Spacer(Modifier.width(MrTheme.spacing.md))
        }

        Column(Modifier.weight(1f)) {
            MrTitle(text = title, maxLines = 1)
            Spacer(Modifier.height(2.dp))
            MrSubtitle(text = subtitle, maxLines = 1)
        }

        if (trailingIcon != null) {
            Spacer(Modifier.width(MrTheme.spacing.sm))
            Box(
                Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = MrRipple.create(MrTheme.colors.onSurface.copy(alpha = 0.12f)),
                        enabled = onMoreClick != null,
                        onClick = { onMoreClick?.invoke() },
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Box(Modifier.size(20.dp)) { trailingIcon() }
            }
        }
    }
}

@Composable
fun MrArtistItem(
    name: String,
    meta: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    thumbnail: @Composable (() -> Unit)? = null,
) {
    val source = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = source,
                indication = MrRipple.create(MrTheme.colors.onSurface.copy(alpha = 0.08f)),
                onClick = onClick,
            )
            .padding(horizontal = MrTheme.spacing.listItemH, vertical = MrTheme.spacing.listItemV),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (thumbnail != null) {
            Box(
                Modifier.size(MrTheme.spacing.thumbMd)
                    .clip(RoundedCornerShape(999.dp)),
            ) { thumbnail() }
            Spacer(Modifier.width(MrTheme.spacing.md))
        }
        Column(Modifier.weight(1f)) {
            MrTitle(text = name, maxLines = 1)
            Spacer(Modifier.height(2.dp))
            MrSubtitle(text = meta, maxLines = 1)
        }
    }
}
