package com.maxrave.maxregnerui.tokens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class MrSpacing(
    val xxs:    Dp = 2.dp,
    val xs:     Dp = 4.dp,
    val sm:     Dp = 8.dp,
    val md:     Dp = 12.dp,
    val lg:     Dp = 16.dp,
    val xl:     Dp = 20.dp,
    val xxl:    Dp = 24.dp,
    val xxxl:   Dp = 32.dp,
    val huge:   Dp = 48.dp,
    val giant:  Dp = 64.dp,

    val screen:         Dp = 16.dp,
    val cardPadding:    Dp = 12.dp,
    val listItemH:      Dp = 16.dp,
    val listItemV:      Dp = 8.dp,
    val bottomBarH:     Dp = 64.dp,
    val miniPlayerH:    Dp = 72.dp,
    val appBarH:        Dp = 56.dp,
    val thumbSm:        Dp = 40.dp,
    val thumbMd:        Dp = 52.dp,
    val thumbLg:        Dp = 72.dp,
)

val LocalMrSpacing = staticCompositionLocalOf { MrSpacing() }
