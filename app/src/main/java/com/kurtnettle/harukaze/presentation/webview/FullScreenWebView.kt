package com.kurtnettle.harukaze.presentation.webview

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun FullScreenWebView(
    url: String,
    innerPadding: PaddingValues,
    onBackRequested: () -> Unit
) {
    var currentUrl by rememberSaveable { mutableStateOf(url) }
    WebViewScreen(
        url = url,
        modifier = Modifier.padding(innerPadding),
        onBackRequested = onBackRequested,
        onUrlChanged = { currentUrl = it }
    )
}