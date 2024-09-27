package com.ssafy.ar.data

data class ScriptInfo(
    val id: Int = 0, // 스크립트 ID
    val name: String = "",
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