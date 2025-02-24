package com.kurtnettle.harukaze.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    val projects: List<ProjectData>
)
