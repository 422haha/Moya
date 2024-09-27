package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class Npc(
    val id: Long,
    val name: String,
    val positions: List<LatLng>,
)
