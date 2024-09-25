package com.ssafy.ar.data

import androidx.compose.runtime.Immutable

@Immutable
data class NearestNPCInfo(
    val nearestNPC: NPCLocation? = null,
    val nearestNPCDistance: Float? = null,
    val shouldPlaceNode: Boolean = false
)