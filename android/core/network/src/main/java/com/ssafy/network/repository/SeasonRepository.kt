package com.ssafy.network.repository

import com.ssafy.model.Park
import com.ssafy.model.SpeciesInSeason
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface SeasonRepository {
    suspend fun getSpeciesInSeason(): Flow<ApiResponse<List<SpeciesInSeason>>>

    suspend fun getParksBySpeciesId(speciesId: Long): Flow<ApiResponse<List<Park>>>
}
