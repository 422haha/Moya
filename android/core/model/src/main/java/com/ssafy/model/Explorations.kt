package com.ssafy.model

import java.util.Date

data class Explorations(
    val explorationId: Long,
    val parkName: String,
    val startTime: Date,
    val distance: Double,
    val collected: Int,
    val imageUrl: String,
    val duration: Int,
    val questCompletedCount: Int,
)
