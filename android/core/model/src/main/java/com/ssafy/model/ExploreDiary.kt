package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class ExploreDiary(
    val message: String,
    val data: List<Explorations>,
)
