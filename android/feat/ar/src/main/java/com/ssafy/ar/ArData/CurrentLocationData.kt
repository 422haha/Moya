package com.ssafy.ar.ArData

import androidx.compose.runtime.Immutable

@Immutable
data class CurrentLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val horizontalAccuracy: Double = 0.0,
    val verticalAccuracy: Double = 0.0,
    val distances: List<Float> = listOf()
)