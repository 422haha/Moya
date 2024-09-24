package com.ssafy.ui

fun formatDistance(info: String): String {
    val distance = info.toIntOrNull() ?: return info
    return if (distance >= 1000) {
        val km = distance / 1000
        val meters = distance % 1000
        "${km}.${String.format("%02d", meters / 10)}km"
    } else {
        "${distance}m"
    }
}