package com.ssafy.ar.data

import com.google.android.gms.location.Priority

data class LocationPriority(
    val priority: Int = Priority.PRIORITY_BALANCED_POWER_ACCURACY,
    val distance: Float = 5f,
    val millisecond: Long = 10000L
)
