package com.ssafy.ar.data

data class ARNode(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val model: String,
    var isQuest: Boolean
)