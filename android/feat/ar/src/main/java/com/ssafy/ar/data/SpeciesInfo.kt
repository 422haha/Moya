package com.ssafy.ar.data

import com.ssafy.ar.R

enum class SpeciesType(val type: Long) {
    FOXTAIL(0L), MAPLE(1L), CONE(2L);

    companion object {
        fun fromLong(value: Long): SpeciesType? = entries.find { it.type == value }
    }
}

fun SpeciesType.getImageResource(): Int {
    return when (this) {
        SpeciesType.MAPLE -> R.drawable.maple
        SpeciesType.FOXTAIL -> R.drawable.foxtail
        SpeciesType.CONE -> R.drawable.cone
    }
}