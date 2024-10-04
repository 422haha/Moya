package com.ssafy.network.request

data class RegisterSpeciesRequestBody(
    val speciesId: Long,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
)
