package com.ssafy.network.repository

import com.ssafy.model.SpeciesInSeason
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun getSpeciesInSeason(): Flow<ApiResponse<List<SpeciesInSeason>>>
}