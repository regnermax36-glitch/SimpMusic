package com.maxrave.maxregnerui.components.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import com.maxrave.maxregnerui.MrTheme
import com.maxrave.maxregnerui.components.MrRipple

data class MrNavItem(
    val label: String,
    val icon: @Composable () -> Unit,
    val selectedIcon: @Composable () -> Unit = icon,
)

/**
 * Custom bottom navigation bar — no Material NavigationBar dependency.
 * Built entirely from Box / Row / BasicText / Canvas shapes.
 */
@Composable
fun MrBottomBar(
    items: List<MrNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MrTheme.colors.surface),
    ) {
        Box(
            Modifier.fillMaxWidth().height(1.dp)
                .background(MrTheme.colors.outline.copy(alpha = 0.18f)),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MrTheme.spacing.bottomBarH)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == selectedIndex
                val source = remember(index) { MutableInteractionSource() }

                val iconColor by animateColorAsState(
                    targetValue = if (selected) MrTheme.colors.primary else MrTheme.colors.onSurfaceMuted,
                    animationSpec = tween(220),
                    label = "nav.color.$index",
                )
                val scale = remember(index) { Animatable(1f) }
                LaunchedEffect(selected) {
                    if (selected) {
                        scale.animateTo(1.15f, tween(120))
                        scale.animateTo(1.0f, tween(120))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = source,
                            indication = MrRipple.create(MrTheme.colors.primary.copy(alpha = 0.15f)),
                            onClick = { onItemSelected(index) },
                        )
                        .padding(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (selected) {
                            Box(
                                Modifier
                                    .size(width = 48.dp, height = 28.dp)
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(MrTheme.colors.primaryMuted),
                            )
                        }
                        Box(
                            Modifier.size(22.dp).scale(scale.value),
                        ) {
                            if (selected) item.selectedIcon() else item.icon()
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                    BasicText(
                        text = item.label,
                        style = MrTheme.typography.labelSmall.copy(color = iconColor),
                        maxLines = 1,
                    )
                }
            }
        }
    }
}
