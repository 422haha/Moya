package com.ssafy.network.repository

import com.ssafy.model.ExploreDiary
import com.ssafy.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface ExploreDiaryRepository {
    suspend fun getExploreDiaryList(
        page: Int,
        size: Int,
    ): Flow<ApiResponse<ExploreDiary>>
}
