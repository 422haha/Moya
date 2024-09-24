package com.ssafy.model.encyclopedialist

data class EncyclopediaItem(
    val discovered: Boolean,
    val imageUrl: String,
    val speciesId: Long,
    val speciesName: String
)