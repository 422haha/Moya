package com.ssafy.ar.data

import androidx.compose.runtime.Immutable

sealed interface QuestState {
    data object WAIT : QuestState
    data object PROGRESS : QuestState
    data object COMPLETE : QuestState
}

@Immutable
data class QuestInfo(
    val id: Long = 0, // 고유 퀘스트 ID
    val npcId: Long = 0, // NPC ID
    val npcPosId: Long = 0,
    val latitude: Double = 0.0, // 위도
    val longitude: Double = 0.0, // 경도
    val questType: Int = 0,
    val speciesId: String = "",
    val isComplete: QuestState = QuestState.WAIT, // 퀘스트 상태(전중후)
    val isPlace: Boolean = false)

