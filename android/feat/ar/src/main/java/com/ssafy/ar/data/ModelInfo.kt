package com.ssafy.ar.data

enum class ModelType(val id: Long, val modelUrl: String) {
    DEFAULT(0L, "models/quest.glb"),
    OTTER(1L, "models/otter.glb"),
    SWORD_OTTER(2L, "models/swordotter.glb"),
    WISH_OTTER(3L, "models/wishotter.glb"),
    RACOON(4L, "models/racoon.glb"),
    MAGIC_RACOON(5L, "models/magicracoon.glb"),
    CHICK(6L, "models/chick.glb"),
    TURTLE(7L, "models/turtle.glb"),
    BROWN_TURTLE(8L, "models/brownturtle.glb"),
    RABBIT(9L, "models/rabbit.glb"),
    DIRECTION(10L, "models/direction.glb"),
    PENGUIN(11L, "models/penguin.glb"),
    SAILFISH(12L, "models/sailfish.glb");

    companion object {
        fun fromLong(id: Long): ModelType = entries.find { it.id == id } ?: DEFAULT
    }
}

fun getModelUrl(id: Long): String {
    return ModelType.fromLong(id).modelUrl
}