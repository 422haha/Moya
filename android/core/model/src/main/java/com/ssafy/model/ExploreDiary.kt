package com.ssafy.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExploreDiary(
    @SerialName("explorations") val data: List<Explorations>,
)
