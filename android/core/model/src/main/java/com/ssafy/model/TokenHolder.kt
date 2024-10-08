package com.ssafy.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenHolder(
    val accessToken: String,
    val refreshToken: String,
)
