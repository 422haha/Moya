package com.ssafy.ar.data

import androidx.compose.runtime.Immutable
import com.google.android.gms.maps.model.LatLng

@Immutable
data class CurrentLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val altitude: Double = 0.0,
    val horizontalAccuracy: Double = 0.0,
    val verticalAccuracy: Double = 0.0,
    val distances: List<Float> = listOf()
)

@Immutable
data class NPCLocation(val id: String, val latLng: LatLng, val radius: Float = 5f, val isPlace: Boolean)
