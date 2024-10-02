package com.ssafy.network.repositoryImpl

import com.ssafy.model.ExploreDiary
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.ExploreDiaryApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.ExploreDiaryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExploreDiaryRepositoryImpl
    @Inject
    constructor(
        private val exploreDiaryApi: ExploreDiaryApi,
    ) : ExploreDiaryRepository {
        override suspend fun getExploreDiaryList(
            page: Int,
            size: Int,
        ): Flow<ApiResponse<ExploreDiary>> =
            flow {
                val response =
                    apiHandler {
                        exploreDiaryApi.getExploreDiaryList(page, size)
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
