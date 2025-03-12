package com.kurtnettle.harukaze.utils

fun isNewerAppVersion(latest: String, current: String): Boolean {
    val cleanLatest = latest.removePrefix("v").removeSuffix("-DEBUG")
    val cleanCurrent = current.removePrefix("v").removeSuffix("-DEBUG")

    val latestParts = cleanLatest.split(".").map { it.toIntOrNull() ?: 0 }
    val currentParts = cleanCurrent.split(".").map { it.toIntOrNull() ?: 0 }

    if (latestParts[0] > currentParts[0]) return true
    if (latestParts[0] < currentParts[0]) return false

    return latestParts.getOrElse(1) { 0 } > currentParts.getOrElse(1) { 0 }
}