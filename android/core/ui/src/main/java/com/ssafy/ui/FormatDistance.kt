package com.ssafy.ui

import java.util.Locale

fun formatDistance(info: String): String {
    val distance = info.toDoubleOrNull() ?: return info
    return if (distance >= 1000) {
        val km = distance / 1000
        String.format(Locale.KOREA, "%.1fkm", km)
    } else {
        "${distance.toInt()}m"
    }
}