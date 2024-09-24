package com.ssafy.model.encyclopediadetail

data class EncyclopediaData(
    val collectedAt: String,
    val description: String,
    val imageUrl: String,
    val imageUrlSmall: String,
    val itemId: String,
    val location: Location,
    val speciesName: String,
    val userPhotos: List<UserPhoto>
)