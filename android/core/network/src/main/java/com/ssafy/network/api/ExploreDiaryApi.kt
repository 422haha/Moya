package com.ssafy.network.api

import com.ssafy.model.ExploreDiary
import com.ssafy.network.ApiResponse

interface ExploreDiaryApi {
    suspend fun getExploreDiaryList(): ApiResponse<ExploreDiary>
}
