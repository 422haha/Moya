package com.ssafy.network.repository

import com.ssafy.model.ParkDetail
import com.ssafy.model.ParkList
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ParkRepository {
    suspend fun getParkList(
        page: Int,
        size: Int,
        latitude: Double,
        longitude: Double,
    ): Flow<ApiResponse<ParkList>>

    suspend fun getPark(
        parkId: Long,
    ): Flow<ApiResponse<ParkDetail>>
}