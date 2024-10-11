package com.ssafy.model.encyclopedialist

import kotlinx.serialization.Serializable

@Serializable
data class Encyclopedia(
    val species: List<EncyclopediaItem>,
    val progress: Double,
)
