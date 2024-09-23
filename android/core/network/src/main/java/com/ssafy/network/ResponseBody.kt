package com.ssafy.network

import kotlinx.serialization.Serializable

@Serializable
data class ResponseBody<T>(
    val message: String? = "",
    val data: T?,
)
