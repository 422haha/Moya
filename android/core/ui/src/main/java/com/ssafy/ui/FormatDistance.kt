package com.ssafy.ui

import java.util.Locale

fun String.formatDistance(): String {
    val distance = this.toDoubleOrNull() ?: return this
    return if (distance >= 1000) {
        val km = distance / 1000f
        String.format(Locale.KOREA, "%.1fkm", km)
    } else {
        String.format(Locale.KOREA, "%.0fm", distance)
    }
}
