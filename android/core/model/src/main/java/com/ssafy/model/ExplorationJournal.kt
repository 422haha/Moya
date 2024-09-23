package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class ExplorationJournalRecent(
    val explorationId: Long,
    val startDate: String,
    val parkId: Long,
    val parkName: String,
    val imageUrl: String,
    val imageUrlSmall: String?,
)
