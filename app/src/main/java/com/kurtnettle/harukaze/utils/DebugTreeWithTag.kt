package com.kurtnettle.harukaze.utils

import timber.log.Timber

internal class DebugTreeWithTag : Timber.DebugTree() {
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        super.log(priority, "Harukaze-${tag ?: "DEBUG"}", message, t)
    }
}
