package com.maxrave.maxregnerui.components

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.maxrave.maxregnerui.MrTheme
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.node.invalidateDraw

/**
 * Custom ripple indication — no Material dependency.
 * Draws a radial circle from the press point that fades and expands on release.
 */
object MrRipple {
    fun create(color: Color = Color.White.copy(alpha = 0.18f)): IndicationNodeFactory =
        MrRippleNodeFactory(color)
}

private class MrRippleNodeFactory(private val color: Color) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode =
        MrRippleNode(interactionSource, color)

    override fun equals(other: Any?) = other is MrRippleNodeFactory && other.color == color
    override fun hashCode() = color.hashCode()
}

private class MrRippleNode(
    private val interactionSource: InteractionSource,
    private val color: Color,
) : Modifier.Node(), DrawModifierNode {

    private val alpha = Animatable(0f)
    private val radius = Animatable(0f)
    private var center by mutableStateOf(Offset.Zero)
    private var job: Job? = null

    override fun onAttach() {
        job = coroutineScope.launch {
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        center = interaction.pressPosition
                        alpha.snapTo(0.35f)
                        radius.snapTo(0f)
                        launch { radius.animateTo(1f, tween(300)) }
                    }
                    is PressInteraction.Release,
                    is PressInteraction.Cancel -> {
                        alpha.animateTo(0f, tween(200))
                    }
                }
            }
        }
    }

    override fun onDetach() {
        job?.cancel()
    }

    override fun ContentDrawScope.draw() {
        drawContent()
        val r = (size.maxDimension / 2f) * radius.value * 1.4f
        if (r > 0f && alpha.value > 0f) {
            drawCircle(
                color = color.copy(alpha = color.alpha * alpha.value),
                radius = r,
                center = center,
            )
        }
    }
}
