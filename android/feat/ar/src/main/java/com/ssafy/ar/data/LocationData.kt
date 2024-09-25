package com.ssafy.ar.data

import androidx.compose.runtime.Immutable
import com.google.android.gms.maps.model.LatLng

@Immutable
data class NPCLocation(val id: String,
                       val latLng: LatLng,
                       val radius: Float = 5f,
                       val isPlace: Boolean)
