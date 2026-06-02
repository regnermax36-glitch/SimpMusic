package com.maxrave.simpmusic.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.maxrave.simpmusic.ui.navigation.destination.home.HomeDestination
import com.maxrave.simpmusic.ui.navigation.destination.library.LibraryDestination
import com.maxrave.simpmusic.ui.navigation.destination.search.SearchDestination
import com.maxrave.simpmusic.ui.theme.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import simpmusic.composeapp.generated.resources.*
import kotlin.reflect.KClass

// ─── Aurora Bottom Navigation Bar ─────────────────────────────────────────────

@Composable
fun AppBottomNavigationBar(
    startDestination: Any = HomeDestination,
    navController: NavController,
    isTranslucentBackground: Boolean = false,
    reloadDestinationIfNeeded: (KClass<*>) -> Unit = {},
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDest = currentBackStackEntry?.destination

    val screens = listOf(BottomNavScreen.Home, BottomNavScreen.Search, BottomNavScreen.Library)

    var selectedIndex by rememberSaveable {
        mutableIntStateOf(
            when (startDestination) {
                is SearchDestination  -> BottomNavScreen.Search.ordinal
                is LibraryDestination -> BottomNavScreen.Library.ordinal
                else                  -> BottomNavScreen.Home.ordinal
            }
        )
    }

    // Sync selected index with back-stack
    LaunchedEffect(currentDest) {
        when {
            currentDest?.hierarchy?.any { it.hasRoute(HomeDestination::class) } == true    -> selectedIndex = 0
            currentDest?.hierarchy?.any { it.hasRoute(SearchDestination::class) } == true  -> selectedIndex = 1
            currentDest?.hierarchy?.any { it.hasRoute(LibraryDestination::class) } == true -> selectedIndex = 2
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                Brush.verticalGradient(
                    listOf(Color.Transparent, SpaceBlack.copy(alpha = 0.95f), SpaceBlack)
                )
            )
            .padding(bottom = 8.dp)
    ) {
        // Frosted-glass pill container
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(32.dp))
                .background(ElevatedSurface.copy(alpha = 0.9f))
                .border(1.dp, BorderSubtle, RoundedCornerShape(32.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                screens.forEachIndexed { index, screen ->
                    val isSelected = selectedIndex == index

                    val iconScale by animateFloatAsState(
                        targetValue  = if (isSelected) 1.15f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                        label = "navIconScale",
                    )
                    val iconTint by animateColorAsState(
                        targetValue  = if (isSelected) Aurora1 else TextTertiary,
                        animationSpec = tween(250),
                        label = "navIconTint",
                    )
                    val labelColor by animateColorAsState(
                        targetValue  = if (isSelected) Aurora1 else TextTertiary,
                        animationSpec = tween(250),
                        label = "navLabelColor",
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {
                                if (isSelected) {
                                    reloadDestinationIfNeeded(screen.route::class)
                                } else {
                                    selectedIndex = index
                                    screen.navigate(navController)
                                }
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            // Glow behind selected icon
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Aurora1.copy(alpha = 0.15f))
                                        .blur(6.dp)
                                )
                            }
                            Icon(
                                painter = painterResource(screen.icon),
                                contentDescription = stringResource(screen.label),
                                tint = iconTint,
                                modifier = Modifier
                                    .size(24.dp)
                                    .scale(iconScale),
                            )
                        }
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text  = stringResource(screen.label),
                            style = androidx.compose.ui.text.TextStyle(
                                fontSize   = 10.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                color      = labelColor,
                            ),
                        )
                        // Active indicator dot
                        Spacer(Modifier.height(3.dp))
                        val dotSize by animateDpAsState(
                            targetValue  = if (isSelected) 4.dp else 0.dp,
                            animationSpec = spring(stiffness = Spring.StiffnessMedium),
                            label = "navDot",
                        )
                        Box(
                            modifier = Modifier
                                .size(dotSize)
                                .clip(CircleShape)
                                .background(AuroraBrush)
                        )
                    }
                }
            }
        }
    }
}
