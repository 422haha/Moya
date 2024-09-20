package com.ssafy.network.request

import com.ssafy.model.LatLng
import kotlinx.serialization.Serializable

@Serializable
data class ExplorationEndRequestBody(
    val route: List<LatLng>,
    val steps: Int,
)
