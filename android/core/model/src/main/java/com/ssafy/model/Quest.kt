package com.ssafy.model

data class Quest(
    val questId: Long,
    val npcId: Long,
    val npcName: String,
    val longitude: Double, // npc의 위치
    val latitude: Double, // npc의 위치
    val questType: Int,
    val speciesId: Long,
    val speciesName: String,
    val completed: Boolean,
)
