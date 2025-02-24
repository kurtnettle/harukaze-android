package com.kurtnettle.harukaze.presentation.main

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kurtnettle.harukaze.SnackbarController
import com.kurtnettle.harukaze.presentation.about.AboutScreen
import com.kurtnettle.harukaze.presentation.about.AboutViewModel
import com.kurtnettle.harukaze.presentation.components.AnimatedTopAppBar
import com.kurtnettle.harukaze.presentation.components.CustomSnackbar
import com.kurtnettle.harukaze.presentation.components.NormalTopAppBar
import com.kurtnettle.harukaze.presentation.home.HomeScreen
import com.kurtnettle.harukaze.presentation.home.isUrlValid
import com.kurtnettle.harukaze.presentation.home.openUrl
import com.kurtnettle.harukaze.presentation.navigation.BottomNavigationBar
import com.kurtnettle.harukaze.presentation.navigation.Screen
import com.kurtnettle.harukaze.presentation.navigation.Screen.Companion.screens
import com.kurtnettle.harukaze.presentation.webview.FullScreenWebView
import com.kurtnettle.harukaze.presentation.webview.FullScreenWebViewTopBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MainScreen(isVisible: Boolean) {
    val aboutViewModel: AboutViewModel = koinViewModel()

    val snackbarHostState = remember { SnackbarHostState() }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage = 0) { screens.size }

    var showWebView by rememberSaveable { mutableStateOf(false) }
    var selectedUrl by rememberSaveable { mutableStateOf("https://duckduckgo.com/") }

    val isTopBarAnimEnabled by aboutViewModel.isTopBarAnimEnabled.collectAsStateWithLifecycle()
    val isWebViewEnabled by aboutViewModel.isWebViewEnabled.collectAsStateWithLifecycle()

    val displayUrl = remember(selectedUrl) {
        try {
            Uri.parse(selectedUrl).host?.removePrefix("www.") ?: selectedUrl
        } catch (e: Exception) {
            Timber.e(e, "Failed to parse URL: ${e.message}")
            selectedUrl
        }
    }

    LaunchedEffect(Unit) {
        SnackbarController.events.collect { event ->
            Timber.d("$event")
            snackbarHostState.showSnackbar(event.message)
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(initialOffsetX = { it / 4 }) +
                scaleIn(initialScale = 0.95f) +
                fadeIn(animationSpec = tween(600)),
        exit = scaleOut(targetScale = 1.05f) +
                fadeOut(animationSpec = tween(400))
    ) {
        Scaffold(modifier = Modifier.fillMaxSize(),
            snackbarHost = { CustomSnackbar(snackbarHostState) },
            topBar = {
                if (!showWebView) {
                    if (isTopBarAnimEnabled) AnimatedTopAppBar() else NormalTopAppBar()
                } else {
                    FullScreenWebViewTopBar(
                        currentUrl = selectedUrl,
                        displayUrl = displayUrl,
                        onBackRequested = { showWebView = false }
                    )
                }
            },
            bottomBar = { if (!showWebView) BottomNavigationBar(pagerState = pagerState) }
        ) { innerPadding ->

            AnimatedVisibility(
                visible = showWebView,
                enter = slideInHorizontally(initialOffsetX = { it / 4 }) +
                        fadeIn(animationSpec = tween(200)),
                exit = slideOutHorizontally(targetOffsetX = { it / 4 }) +
                        fadeOut(animationSpec = tween(200))
            ) {
                FullScreenWebView(
                    url = selectedUrl,
                    innerPadding = innerPadding,
                    onBackRequested = { showWebView = false }
                )
            }

            AnimatedVisibility(
                visible = !showWebView,
                enter = slideInHorizontally(initialOffsetX = { it / 4 }) +
                        fadeIn(animationSpec = tween(200)),
                exit = fadeOut(animationSpec = tween(200))
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    key = { page -> screens[page].route },
                ) { page ->
                    when (screens[page]) {
                        Screen.About -> AboutScreen()
                        Screen.Home -> HomeScreen(
                            onCardClick = { url ->
                                if (isWebViewEnabled) {
                                    if (!isUrlValid(url)) coroutineScope.launch {
                                        SnackbarController.sendError("Invalid URL: ${url.take(50)}")
                                    } else {
                                        selectedUrl = url
                                        showWebView = true
                                    }
                                } else {
                                    coroutineScope.launch {
                                        openUrl(context, url)
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}