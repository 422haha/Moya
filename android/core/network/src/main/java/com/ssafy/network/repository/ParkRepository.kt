package com.ssafy.network.repository

import com.ssafy.model.Park
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

    suspend fun getPark(parkId: Long): Flow<ApiResponse<ParkDetail>>

    /**
     * 가장 가까운 공원 하나
     */
    suspend fun getClosePark(
        latitude: Double,
        longitude: Double,
    ): Flow<ApiResponse<Park>>

    /**
     * 인기 공원 목록
     */
    suspend fun getFamousParks(
        latitude: Double,
        longitude: Double,
    ): Flow<ApiResponse<List<Park>>>
}
