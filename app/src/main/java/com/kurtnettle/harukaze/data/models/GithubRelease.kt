package com.kurtnettle.harukaze.data.models

import kotlinx.serialization.Serializable

@Serializable
data class GithubRelease(
    val tag_name: String,
    val name: String,
    val published_at: String,
    val html_url: String
)