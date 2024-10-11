package com.ssafy.model.encyclopediadetail

import kotlinx.serialization.Serializable

@Serializable
data class EncyclopediaData(
    val collectedAt: String? = null,
    val description: String,
    val imageUrl: String,
    val itemId: Long,
    val location: Location? = null,
    val speciesName: String,
    val userPhotos: List<UserPhoto>,
)
