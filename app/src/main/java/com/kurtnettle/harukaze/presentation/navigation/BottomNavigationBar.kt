package com.kurtnettle.harukaze.presentation.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.kurtnettle.harukaze.SnackbarController
import com.kurtnettle.harukaze.presentation.navigation.Screen.Companion.pageIndex
import com.kurtnettle.harukaze.presentation.navigation.Screen.Companion.screens
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun BottomNavigationBar(pagerState: PagerState) {
    val coroutineScope = rememberCoroutineScope()
    val scrollMutex = remember { Mutex() }

    val currentScreen by remember {
        derivedStateOf {
            screens.getOrNull(pagerState.currentPage) ?: Screen.Home
        }
    }

    NavigationBar {
        screens.forEach { screen ->
            NavigationBarItem(
                selected = currentScreen == screen,
                onClick = {
                    coroutineScope.launch {
                        handleNavigation(
                            targetPage = screen.pageIndex,
                            pagerState = pagerState,
                            scrollMutex = scrollMutex
                        )
                    }
                },
                icon = {
                    AnimatedNavIcon(
                        screen = screen,
                        isSelected = currentScreen == screen
                    )
                },
                label = {
                    NavLabel(
                        text = stringResource(screen.titleRes),
                        isSelected = currentScreen == screen
                    )
                }
            )
        }
    }
}

suspend fun handleNavigation(
    targetPage: Int,
    pagerState: PagerState,
    scrollMutex: Mutex,
) {
    try {
        scrollMutex.withLock {
            pagerState.animateScrollToPage(targetPage)
        }
    } catch (e: Exception) {
        if (e !is CancellationException) {
            SnackbarController.sendError(
                "Navigation failed: ${e.message ?: "Unknown error"}"
            )
        }
    }
}

@Composable
private fun AnimatedNavIcon(screen: Screen, isSelected: Boolean) {
    AnimatedContent(
        targetState = isSelected,
        transitionSpec = {
            fadeIn(tween(150)) +
                    scaleIn(initialScale = 0.8f) togetherWith fadeOut(tween(150)) +
                    scaleOut(targetScale = 0.8f)
        }) { selected ->
        Icon(
            imageVector = if (selected) screen.selectedIcon else screen.unselectedIcon,
            contentDescription = stringResource(screen.titleRes),
        )
    }
}

@Composable
private fun NavLabel(text: String, isSelected: Boolean) {
    Text(
        text = text.ifEmpty { "Untitled Screen" },
        modifier = if (isSelected) Modifier.animateContentSize() else Modifier
    )
}