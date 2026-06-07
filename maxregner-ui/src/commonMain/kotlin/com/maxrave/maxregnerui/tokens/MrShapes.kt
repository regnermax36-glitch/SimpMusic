package com.maxrave.maxregnerui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class MrShapes(
    val none:       Dp = 0.dp,
    val xs:         Dp = 4.dp,
    val sm:         Dp = 8.dp,
    val md:         Dp = 12.dp,
    val lg:         Dp = 16.dp,
    val xl:         Dp = 20.dp,
    val xxl:        Dp = 28.dp,
    val full:       Dp = 999.dp,

    // Semantic aliases
    val card:       Dp = 12.dp,
    val cardLarge:  Dp = 20.dp,
    val chip:       Dp = 999.dp,
    val button:     Dp = 999.dp,
    val thumbnail:  Dp = 8.dp,
    val avatar:     Dp = 999.dp,
    val bottomSheet:Dp = 20.dp,
    val miniPlayer: Dp = 16.dp,
    val badge:      Dp = 999.dp,
)

val LocalMrShapes = staticCompositionLocalOf { MrShapes() }
