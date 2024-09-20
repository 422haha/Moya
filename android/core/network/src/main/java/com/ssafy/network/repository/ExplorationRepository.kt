package com.ssafy.network.repository

import com.ssafy.network.ApiResponse

interface ExplorationRepository {
    suspend fun startExploration(parkId: Long): ApiResponse<com.ssafy.model.ExplorationData>


}
