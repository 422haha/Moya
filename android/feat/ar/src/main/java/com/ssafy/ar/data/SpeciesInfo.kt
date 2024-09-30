package com.ssafy.ar.data

import com.ssafy.ar.R

enum class SpeciesType(val type: Long) {
    MAPLE(1L), FOXTAIL(2L), CONE(3L);

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