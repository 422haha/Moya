package com.ssafy.network.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val provider: String,
    val accessToken: String,
)
