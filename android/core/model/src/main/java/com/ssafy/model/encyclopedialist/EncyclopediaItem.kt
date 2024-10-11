package com.ssafy.model.encyclopedialist

import kotlinx.serialization.Serializable

@Serializable
data class EncyclopediaItem(
    val discovered: Boolean,
    val imageUrl: String,
    val speciesId: Long,
    val speciesName: String
)