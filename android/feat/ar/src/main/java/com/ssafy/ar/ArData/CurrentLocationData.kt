package com.ssafy.ar.ArData

data class CurrentLocation(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var altitude: Double = 0.0,
    var horizontalAccuracy: Double = 0.0,
    var verticalAccuracy: Double = 0.0,
    val distances: MutableList<Float> = mutableListOf()
)