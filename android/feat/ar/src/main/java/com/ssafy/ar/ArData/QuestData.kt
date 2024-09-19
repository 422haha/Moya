package com.ssafy.ar.ArData

sealed interface QuestStatus {
    data object WAIT : QuestStatus
    data object PROGRESS : QuestStatus
    data object COMPLETE : QuestStatus
}

data class QuestData(
    val id: String = "",
    val model: String = "",
    val title: String = "",
    val prevDescription: String = "",
    val middleDescription: String = "",
    val nextDescription: String = "",
    val prevCheckDescription: String = "",
    val middleCheckDescription: String = "",
    val nextCheckDescription: String = "",
    val completeMessage: String = "",
    var isComplete: QuestStatus = QuestStatus.WAIT
)

