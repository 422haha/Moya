package com.ssafy.network.repositoryImpl

import com.ssafy.model.encyclopediadetail.EncyclopediaDetail
import com.ssafy.model.encyclopedialist.EncyclopediaList
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.EncyclopediaApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.EncyclopediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EncyclopediaRepositoryImpl @Inject constructor(
    private val encyclopediaApi: EncyclopediaApi
) : EncyclopediaRepository {
    override suspend fun getEncyclopedia(parkId: Long): Flow<ApiResponse<EncyclopediaList>> {
        return flow {
            val response = apiHandler {
                encyclopediaApi.getEncyclopedia(parkId)
            }
            when (response) {
                is ApiResponse.Success -> {
                    emit(ApiResponse.Success(response.body?.data))
                }

                is ApiResponse.Error -> {
                    emit(
                        ApiResponse.Error(
                            errorCode = response.errorCode,
                            errorMessage = response.errorMessage
                        )
                    )
                }
            }
        }
    }

    override suspend fun getEncyclopediaDetail(parkId: Long): Flow<ApiResponse<EncyclopediaDetail>> {
        return flow {
            val response = apiHandler {
                encyclopediaApi.getEncyclopediaDetail(parkId)
            }
            when (response) {
                is ApiResponse.Success -> {
                    emit(ApiResponse.Success(response.body?.data))
                }

                is ApiResponse.Error -> {
                    emit(
                        ApiResponse.Error(
                            errorCode = response.errorCode,
                            errorMessage = response.errorMessage
                        )
                    )
                }
            }
        }
    }
}