package com.skele.moya.background.util

import android.location.Location
import com.ssafy.model.LatLng

fun LatLng.toLocation() = Location("").apply {
    latitude = latitude
    longitude = longitude
}

/**
 * 두 좌표 간의 거리 (m 단위)
 */
fun LatLng.distanceTo(latLng: LatLng) = toLocation().distanceTo(latLng.toLocation())
