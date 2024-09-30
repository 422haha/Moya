package com.ssafy.ar.data

import androidx.compose.runtime.Immutable

@Immutable
data class NearestNPCInfo(
    val npc: QuestInfo? = null,
    val distance: Float? = null,
    val shouldPlace: Boolean = false
)