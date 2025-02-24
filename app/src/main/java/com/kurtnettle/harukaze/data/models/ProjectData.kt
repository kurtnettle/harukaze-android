package com.kurtnettle.harukaze.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProjectData(
    val title: String,
    val description: String,
    val url: String
)