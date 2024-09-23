package com.ssafy.model

data class Npc(
    val id: Long,
    val name: String,
    val positions: List<LatLng>,
)
