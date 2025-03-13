package com.kurtnettle.harukaze.utils

import android.content.Context
import androidx.webkit.WebViewCompat
import timber.log.Timber

fun isNewerAppVersion(latest: String, current: String): Boolean {
    val cleanLatest = latest.removePrefix("v").removeSuffix("-DEBUG")
    val cleanCurrent = current.removePrefix("v").removeSuffix("-DEBUG")

    val latestParts = cleanLatest.split(".").map { it.toIntOrNull() ?: 0 }
    val currentParts = cleanCurrent.split(".").map { it.toIntOrNull() ?: 0 }

    if (latestParts[0] > currentParts[0]) return true
    if (latestParts[0] < currentParts[0]) return false

    return latestParts.getOrElse(1) { 0 } > currentParts.getOrElse(1) { 0 }
}


fun getWebViewVersion(context: Context): String {
    return try {
        val webViewPackage = WebViewCompat.getCurrentWebViewPackage(context)
        webViewPackage?.versionName ?: "not available"
    } catch (e: Exception) {
        Timber.e("WebViewVersion", "Error getting WebView version", e)
        "Error getting WebView version: ${e.message}"
    }
}