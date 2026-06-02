package com.maxrave.simpmusic.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxrave.simpmusic.ui.theme.*

// ─── Aurora Chip Group ─────────────────────────────────────────────────────────

@Composable
fun AuroraChipGroup(
    chips: List<String>,
    selectedChip: String,
    onChipSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        chips.forEach { chip ->
            AuroraChip(
                text       = chip,
                isSelected = chip == selectedChip,
                onClick    = { onChipSelected(chip) },
            )
        }
    }
}

@Composable
fun AuroraChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue  = if (isSelected) Aurora1 else ElevatedSurface,
        animationSpec = tween(200),
        label = "chipBg",
    )
    val textColor by animateColorAsState(
        targetValue  = if (isSelected) Color.White else TextSecondary,
        animationSpec = tween(200),
        label = "chipText",
    )
    val borderColor by animateColorAsState(
        targetValue  = if (isSelected) Aurora1 else BorderSubtle,
        animationSpec = tween(200),
        label = "chipBorder",
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication        = null,
                onClick           = onClick,
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text       = text,
            fontSize   = 13.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            color      = textColor,
        )
    }
}
