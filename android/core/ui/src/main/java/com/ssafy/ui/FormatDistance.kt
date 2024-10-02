package com.ssafy.ui

import java.util.Locale

//fun formatDistance(info: String): String {
//    val distance = info.toIntOrNull() ?: return info
//    return if (distance >= 1000) {
//        val km = distance / 1000
//        val meters = distance % 1000
//        "${km}.${String.format(Locale.KOREA, "%02d", meters / 10)}km"
//    } else {
//        "${distance}m"
//    }
//}

fun String.formatDistance(): String {
    val distance = this.toIntOrNull() ?: return this
    return if (distance >= 1000) {
        val km = distance / 1000
        String.format(Locale.KOREA, "%.1fkm", km)
    } else {
        "${distance.toInt()}m"
    }
}
