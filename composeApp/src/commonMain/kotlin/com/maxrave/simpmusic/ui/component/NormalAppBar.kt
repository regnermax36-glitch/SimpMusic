package com.maxrave.simpmusic.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.maxrave.simpmusic.ui.theme.*

// ─── Aurora Normal App Bar ─────────────────────────────────────────────────────

@Composable
fun NormalAppBar(
    title: String,
    onBack: (() -> Unit)? = null,
    backIcon: ImageVector? = null,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Back button
        if (onBack != null) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(GlassLight)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication        = null,
                        onClick           = onBack,
                    )
            ) {
                if (backIcon != null) {
                    Icon(
                        imageVector        = backIcon,
                        contentDescription = "Back",
                        tint               = TextPrimary,
                        modifier           = Modifier.size(20.dp),
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
        }

        // Title
        Text(
            text     = title,
            style    = typo().titleLarge,
            modifier = Modifier.weight(1f),
        )

        // Actions
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment     = Alignment.CenterVertically,
            content               = actions,
        )
    }
}
