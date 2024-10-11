package com.ssafy.network.repositoryImpl

import com.ssafy.model.encyclopediadetail.EncyclopediaData
import com.ssafy.model.encyclopedialist.Encyclopedia
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.EncyclopediaApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.EncyclopediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EncyclopediaRepositoryImpl
    @Inject
    constructor(
        private val encyclopediaApi: EncyclopediaApi,
    ) : EncyclopediaRepository {
        override suspend fun getEncyclopediaByParkId(
            parkId: Long,
            page: Int,
            size: Int,
            filter: String,
        ): Flow<ApiResponse<Encyclopedia>> =
            flow {
                val response =
                    apiHandler {
                        encyclopediaApi.getEncyclopediaByParkId(parkId, page, size, filter)
                    }
                when (response) {
                    is ApiResponse.Success -> {
                        emit(ApiResponse.Success(response.body?.data))
                    }

                    is ApiResponse.Error -> {
                        emit(
                            ApiResponse.Error(
                                errorCode = response.errorCode,
                                errorMessage = response.errorMessage,
                            ),
                        )
                    }
                }
            }

        override suspend fun getEncyclopediaDetail(itemId: Long): Flow<ApiResponse<EncyclopediaData>> =
            flow {
                val response =
                    apiHandler {
                        encyclopediaApi.getEncyclopediaDetail(itemId)
                    }
                when (response) {
                    is ApiResponse.Success -> {
                        emit(ApiResponse.Success(response.body?.data))
                    }

                    is ApiResponse.Error -> {
                        emit(
                            ApiResponse.Error(
                                errorCode = response.errorCode,
                                errorMessage = response.errorMessage,
                            ),
                        )
                    }
                }
            }

        override suspend fun getEncyclopediaAll(
            page: Int,
            size: Int,
            filter: String,
        ) = flow {
            val response =
                apiHandler {
                    encyclopediaApi.getEncyclopediaAll(page, size, filter)
                }
            when (response) {
                is ApiResponse.Success -> {
                    emit(ApiResponse.Success(response.body?.data))
                }

                is ApiResponse.Error -> {
                    emit(
                        ApiResponse.Error(
                            errorCode = response.errorCode,
                            errorMessage = response.errorMessage,
                        ),
                    )
                }
            }
        }
    }
