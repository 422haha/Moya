package com.ssafy.network.api

import com.ssafy.model.TokenHolder
import com.ssafy.model.User
import com.ssafy.network.ResponseBody
import com.ssafy.network.request.LoginRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserApi {
    // /user/login
    @POST("/user/login")
    suspend fun login(
        @Body loginRequest: LoginRequest,
    ): Response<ResponseBody<TokenHolder>>

    // /user/name
    @GET("/user/name")
    suspend fun getName(): Response<ResponseBody<User>>
}
