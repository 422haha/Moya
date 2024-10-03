package com.ssafy.network.util

import com.ssafy.network.ApiResponse
import com.ssafy.network.ResponseBody

fun <T : ResponseBody<R>, R : Any> ApiResponse<T>.simplify(): ApiResponse<R> =
    when (this) {
        is ApiResponse.Success -> {
            ApiResponse.Success(this.body?.data)
        }

        is ApiResponse.Error -> {
            ApiResponse.Error(
                errorCode = this.errorCode,
                errorMessage = this.errorMessage,
            )
        }
    }
