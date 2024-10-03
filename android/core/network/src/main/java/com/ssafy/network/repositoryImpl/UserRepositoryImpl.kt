package com.ssafy.network.repositoryImpl

import com.ssafy.model.TokenHolder
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.UserApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.UserRepository
import com.ssafy.network.request.LoginRequest
import com.ssafy.network.util.simplify
import javax.inject.Inject


class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi,
): UserRepository {
    override suspend fun login(provider: String, accessToken: String): ApiResponse<TokenHolder> {
        val response =
            apiHandler {
                userApi.login(LoginRequest(provider, accessToken))
            }
        return response.simplify()
    }
}
