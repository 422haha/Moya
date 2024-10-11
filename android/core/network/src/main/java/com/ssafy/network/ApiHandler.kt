package com.ssafy.network

import com.google.gson.Gson
import retrofit2.Response

suspend fun <T> apiHandler(apiResponse: suspend () -> Response<T>): ApiResponse<T> {
    val result =
        runCatching {
            val response = apiResponse.invoke()
            val errorBody = Gson().fromJson(response.errorBody()?.string(), ErrorBody::class.java)
            if (response.isSuccessful) {
                ApiResponse.Success(response.body())
            } else {
                ApiResponse.Error(response.code(), errorBody.message)
            }
        }
    return result.getOrElse { ApiResponse.Error(errorMessage = it.message ?: "") }
}
