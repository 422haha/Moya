package com.ssafy.network.repository

import com.ssafy.model.TokenHolder
import com.ssafy.network.ApiResponse

interface UserRepository {
    suspend fun login(
        provider: String,
        accessToken: String,
    ): ApiResponse<TokenHolder>
}
