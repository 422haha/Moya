package com.ssafy.network

data class ResponseBody<T>(
    val message: String? = "",
    val data: T?,
)
