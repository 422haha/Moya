package com.ssafy.ar.data

data class NPCInfo(
    val id: Long = 0, // NPC ID
    val name: String = "", // NPC 이름
    val questType: Int = 0, // 퀘스트 종류
    val speciesId: Long = 0, // 식물 ID
    val speciesName: String = "", // 식물 이름
    val prevDescription: String = "",
    val middleDescription: String = "",
    val nextDescription: String = "",
    val prevCheckDescription: String = "",
    val middleCheckDescription: String = "",
    val nextCheckDescription: String = "",
    val completeMessage: String = "",
)

data class ModelInfo(
    val id: Long = 0,
    val modelUrl: String = "models/quest.glb"
)