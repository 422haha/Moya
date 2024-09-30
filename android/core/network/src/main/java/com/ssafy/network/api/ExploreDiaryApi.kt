package com.ssafy.network.api

import com.ssafy.model.ExploreDiary
import com.ssafy.network.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreDiaryApi {
    @GET("diary/diarys")
    suspend fun getExploreDiaryList(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): Response<ResponseBody<ExploreDiary>>
}
