package com.ssafy.network.repositoryImpl

import com.ssafy.model.ParkList
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.ParkApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.ParkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ParkRepositoryImpl @Inject constructor(
    private val parkApi: ParkApi
): ParkRepository {
    override suspend fun getParkList(
        page: Int,
        size: Int,
        latitude: Double,
        longitude: Double
    ): Flow<ApiResponse<ParkList>> = flow {
        val response =
            apiHandler {
                parkApi.getParkList(page, size, latitude, longitude)
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