package com.ssafy.ar.ArData

const val WAIT_QUEST = 1
const val PROGRESS_QUEST = 2
const val COMPLETE_QUEST = 3

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
    var isComplete: Int = WAIT_QUEST
)

