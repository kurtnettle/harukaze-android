package com.kurtnettle.harukaze.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val repo_url: String,
    val google_form_url: String,
    val telegram_group_url: String,
    val app_update_url: String,
    val project_update_url: String
)


fun AppConfig.hasEmptyFields(): Boolean {
    return listOf(
        repo_url,
        google_form_url,
        telegram_group_url,
        app_update_url,
        project_update_url
    ).any { it.isBlank() }
}