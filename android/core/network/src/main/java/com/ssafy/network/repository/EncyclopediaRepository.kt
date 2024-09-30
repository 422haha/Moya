package com.ssafy.network.repository

import com.ssafy.model.encyclopediadetail.EncyclopediaData
import com.ssafy.model.encyclopedialist.Encyclopedia
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface EncyclopediaRepository {
    suspend fun getEncyclopediaByParkId(
        parkId: Long,
        page: Int,
        size: Int,
        filter: String,
    ): Flow<ApiResponse<Encyclopedia>>

    suspend fun getEncyclopediaDetail(itemId: Long): Flow<ApiResponse<EncyclopediaData>>

    suspend fun getEncyclopediaAll(
        page: Int,
        size: Int,
        filter: String,
    ): Flow<ApiResponse<Encyclopedia>>
}
