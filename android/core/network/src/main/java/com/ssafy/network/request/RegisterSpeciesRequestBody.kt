package com.ssafy.network.request

import kotlinx.serialization.Serializable

@Serializable
data class RegisterSpeciesRequestBody(
    val speciesId: Long,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
)
