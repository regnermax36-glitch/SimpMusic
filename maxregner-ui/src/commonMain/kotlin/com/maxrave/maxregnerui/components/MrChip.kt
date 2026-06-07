package com.maxrave.maxregnerui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme

enum class MrChipStyle { Filled, Tonal, Outlined }

@Composable
fun MrChip(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    style: MrChipStyle = MrChipStyle.Tonal,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(MrTheme.shapes.chip)
    val source = remember { MutableInteractionSource() }

    val containerColor by animateColorAsState(
        targetValue = when {
            selected          -> MrTheme.colors.primary
            style == MrChipStyle.Filled  -> MrTheme.colors.surfaceVariant
            style == MrChipStyle.Tonal   -> MrTheme.colors.surfaceElevated
            else              -> Color.Transparent
        },
        animationSpec = tween(180),
        label = "chip.bg",
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) MrTheme.colors.onPrimary else MrTheme.colors.onSurface,
        animationSpec = tween(180),
        label = "chip.label",
    )

    val borderMod = if (style == MrChipStyle.Outlined)
        Modifier.border(1.dp, MrTheme.colors.outline, shape)
    else Modifier

    Box(
        modifier = modifier
            .clip(shape)
            .then(borderMod)
            .background(containerColor)
            .clickable(
                interactionSource = source,
                indication = MrRipple.create(labelColor.copy(alpha = 0.15f)),
                enabled = enabled,
                onClick = onClick,
            )
            .padding(horizontal = 14.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        BasicText(
            text = label,
            style = MrTheme.typography.labelSmall.copy(color = labelColor),
        )
    }
}
