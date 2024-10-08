package com.ssafy.network.repository

import com.ssafy.model.TokenHolder
import com.ssafy.model.User
import com.ssafy.network.ApiResponse

interface UserRepository {
    suspend fun login(
        provider: String,
        accessToken: String,
    ): ApiResponse<TokenHolder>

    suspend fun getName(): ApiResponse<User>
}
