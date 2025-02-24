package com.kurtnettle.harukaze.utils

import java.util.concurrent.TimeUnit

object Constants {
    const val APP_CONFIG_URL = "https://harukaze-app-api.kurtnettle.workers.dev/appConfig"

    /* U Intervals */
    val UPDATE_THROTTLE_INTERVAL = TimeUnit.MINUTES.toMillis(30)
    val APP_CONFIG_UPDATE_INTERVAL = TimeUnit.HOURS.toMillis(12)
    val APP_UPDATE_INTERVAL = TimeUnit.DAYS.toMillis(1)
    val PROJECT_UPDATE_INTERVAL = TimeUnit.DAYS.toMillis(1)
}
