package com.kurtnettle.harukaze.utils

fun isNewerAppVersion(latest: String, current: String): Boolean {

    val latestParts = latest.split(".").map { it.toIntOrNull() ?: 0 }
    val currentParts = current.split(".").map { it.toIntOrNull() ?: 0 }

    for (i in latestParts.indices) {
        if (i >= currentParts.size || latestParts[i] > currentParts[i]) return true
        if (latestParts[i] < currentParts[i]) return false
    }

    return false
}