package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestList(
    val quest: List<Quest>
)

@Serializable
data class Quest(
    val questId: Long,
    val npcId: Long,
    val npcPosId: Long,
    val npcName: String,
    val longitude: Double, // npc의 위치
    val latitude: Double, // npc의 위치
    val questType: Int,
    val speciesId: Long,
    val speciesName: String,
    val completed: Int,
)

@Serializable
data class CompletedQuest(
    val completionDate: String,
    val completedQuest: Int,
)
