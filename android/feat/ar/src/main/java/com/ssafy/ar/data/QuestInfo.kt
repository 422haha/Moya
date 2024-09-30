package com.ssafy.ar.data

import androidx.compose.runtime.Immutable
import com.ssafy.ar.R

enum class QuestType(val type: Int) {
    MAPLE(0), GINKGO(1), CONE(2), ARROW(3);

    companion object {
        fun fromInt(value: Int): QuestType? = entries.find { it.type == value }
    }
}

fun QuestType.getImageResource(): Int {
    return when (this) {
        QuestType.MAPLE -> R.drawable.maple
        QuestType.GINKGO -> R.drawable.ginkgo
        QuestType.CONE -> R.drawable.cone
        QuestType.ARROW -> R.drawable.arrow
    }
}

enum class QuestState(val type: String) {
    WAIT("시작 가능"), 
    PROGRESS("진행중"), 
    COMPLETE("완료")
}

fun QuestState.getImageUrl(): String {
    return when (this) {
        QuestState.WAIT -> "picture/wait.png"
        QuestState.PROGRESS -> "picture/progress.png"
        QuestState.COMPLETE -> "picture/complete.png"
    }
}

@Immutable
data class QuestInfo(
    val id: Long = 0, // 고유 ID
    val npcId: Long = 0, // 랜덤 NPC ID
    val npcPosId: Long = 0, // 랜덤 위치
    val questType: Int = 0, // 랜덤 퀘스트 종류
    val latitude: Double = 0.0, // 위도
    val longitude: Double = 0.0, // 경도
    val speciesId: String = "", // 동식물
    val speciesName: String = "", // 동식물 이름
    val isComplete: QuestState = QuestState.WAIT, // 퀘스트 상태
    val isPlace: Boolean = false)

