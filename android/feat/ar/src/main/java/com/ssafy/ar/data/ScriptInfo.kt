package com.ssafy.ar.data

data class ScriptInfo(
    val id: Int = 0, // 스크립트 ID
    val name: String = "",
    val questType: Int = 0, // 퀘스트 종류
    val speciesId: Long = 0, // 식물 ID
    val speciesName: String = "", // 식물 이름

    val prevDescription: String = "",
    val nextDescription: String = "",
    val prevCheckDescription: String = "",
    val nextCheckDescription: String = "",
    val completeMessage: String = "",
)

enum class ModelType(val id: Long, val modelUrl: String) {
    DEFAULT(0L, "models/quest.glb"),
    BROWN_TURTLE(1L, "models/brownturtle.glb"),
    CHICK(2L, "models/chick.glb"),
    MAGIC_RACOON(3L, "models/magicracoon.glb"),
    OTTER(4L, "models/otter.glb"),
    PENGUIN(5L, "models/penguin.glb"),
    RABBIT(6L, "models/rabbit.glb"),
    RACOON(7L, "models/racoon.glb"),
    SAILFISH(8L, "models/sailfish.glb"),
    SWORD_OTTER(9L, "models/swordotter.glb"),
    TURTLE(10L, "models/turtle.glb"),
    UNICORN(11L, "models/unicorn.glb"),
    WISH_OTTER(12L, "models/wishotter.glb");

    companion object {
        fun fromId(id: Long): ModelType = entries.find { it.id == id } ?: DEFAULT
    }
}