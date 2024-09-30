package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class Explorations(
    val explorationId: Long,
    val parkName: String,
    val startTime: String,
    val distance: Double,
    val collectedCount: Int,
    val imageUrl: String,
    val duration: Int,
    val questCompletedCount: Int,
)
