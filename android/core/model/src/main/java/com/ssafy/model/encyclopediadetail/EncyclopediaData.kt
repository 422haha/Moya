package com.ssafy.model.encyclopediadetail

import kotlinx.serialization.Serializable

@Serializable
data class EncyclopediaData(
    val collectedAt: String,
    val description: String,
    val imageUrl: String,
    val itemId: Long,
    val location: Location,
    val speciesName: String,
    val userPhotos: List<UserPhoto>,
)
