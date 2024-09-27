package com.ssafy.ar.data

import androidx.compose.runtime.Immutable

@Immutable
data class NearestNPCInfo(
    val npc: NPCLocation? = null,
    val distance: Float? = null,
    val shouldPlace: Boolean = false
)