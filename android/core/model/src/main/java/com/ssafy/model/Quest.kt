package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestList(
    val quest: List<Quest>,
)

@Serializable
data class Quest(
    val questId: Long,
    val npcId: Long,
    val npcName: String,
    val npcPosId: Long,
    val longitude: Double,
    val latitude: Double,
    val questType: Int,
    val speciesId: Long,
    val speciesName: String,
    val completed: String,
)

@Serializable
data class CompletedQuest(
    val completionDate: String,
    val completedQuests: Int,
)
