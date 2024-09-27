package com.ssafy.network

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val message: String? = "",
    val data: List<String> = listOf(),
)
