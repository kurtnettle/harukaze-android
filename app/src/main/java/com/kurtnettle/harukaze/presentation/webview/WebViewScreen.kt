package com.kurtnettle.harukaze.presentation.webview

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import timber.log.Timber


@Composable
fun WebViewScreen(
    url: String,
    modifier: Modifier = Modifier,
    onBackRequested: () -> Unit,
    onUrlChanged: (String) -> Unit
) {
    var webView by remember { mutableStateOf<WebView?>(null) }
    val webViewState by rememberSaveable { mutableStateOf<Bundle?>(null) }
    var progress by remember { mutableIntStateOf(0) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress.toFloat() / 100f, animationSpec = tween(durationMillis = 300)
    )

    BackHandler(enabled = true) {
        webView?.let { wv ->
            if (wv.canGoBack()) wv.goBack() else onBackRequested()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            webView?.run {
                stopLoading()
                destroy()
            }
            webView = null
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        AndroidView(
            factory = { ctx ->
                createConfiguredWebView(
                    context = ctx,
                    savedState = webViewState,
                    initialUrl = url,
                    onUrlChanged = onUrlChanged,
                    onProgressChanged = { progress = it }
                )
            }, update = { webView = it }
        )

        if (progress < 100) {
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
            )
        }
    }
}


@SuppressLint("SetJavaScriptEnabled")
private fun createConfiguredWebView(
    context: Context,
    savedState: Bundle?,
    initialUrl: String,
    onUrlChanged: (String) -> Unit,
    onProgressChanged: (Int) -> Unit
): WebView {
    return WebView(context).apply {
        webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                url?.let { onUrlChanged(it) }
            }

            override fun onReceivedError(
                view: WebView?, request: WebResourceRequest?, error: WebResourceError
            ) {
                Timber.e(error.toString(), "Error Occurred.")
                super.onReceivedError(view, request, error)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView, request: WebResourceRequest
            ): Boolean {
                val url = request.url.toString()
                return if (url.startsWith("http", true)) {
                    false
                } else {
                    try {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                        true
                    } catch (e: Exception) {
                        Timber.e(e, e.toString())
                        e.printStackTrace()
                        true
                    }
                }
            }
        }

        webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                onProgressChanged(newProgress)
            }
        }

        configureWebViewSettings()
        configureCookiePolicy()

        savedState?.let { restoreState(it) } ?: loadUrl(initialUrl)
    }
}


@SuppressLint("SetJavaScriptEnabled")
private fun WebView.configureWebViewSettings() {
    settings.apply {
        javaScriptEnabled = true
        domStorageEnabled = true
        cacheMode = WebSettings.LOAD_DEFAULT
        setSupportZoom(true)
        useWideViewPort = true
        loadWithOverviewMode = true
        builtInZoomControls = true
        displayZoomControls = false
        setBackgroundColor(Color.Transparent.toArgb())
    }

    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
}

private fun WebView.configureCookiePolicy() {
    CookieManager.getInstance().apply {
        setAcceptCookie(true)
        setAcceptThirdPartyCookies(this@configureCookiePolicy, true)
    }
}