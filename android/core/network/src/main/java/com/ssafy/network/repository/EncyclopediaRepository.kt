package com.ssafy.network.repository

import com.ssafy.model.encyclopediadetail.EncyclopediaDetail
import com.ssafy.model.encyclopedialist.EncyclopediaList
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface EncyclopediaRepository {
    suspend fun getEncyclopedia(parkId: Long): Flow<ApiResponse<EncyclopediaList>>

    suspend fun getEncyclopediaDetail(parkId: Long): Flow<ApiResponse<EncyclopediaDetail>>
}