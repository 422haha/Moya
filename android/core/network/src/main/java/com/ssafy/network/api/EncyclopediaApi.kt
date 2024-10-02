package com.ssafy.network.api

import com.ssafy.model.encyclopediadetail.EncyclopediaData
import com.ssafy.model.encyclopedialist.Encyclopedia
import com.ssafy.network.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EncyclopediaApi {
    @GET("/collection/{parkId}")
    suspend fun getEncyclopediaByParkId(
        @Path("parkId") parkId: Long,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("filter") filter: String,
    ): Response<ResponseBody<Encyclopedia>>

    @GET("/collection/{itemId}/detail")
    suspend fun getEncyclopediaDetail(
        @Path("itemId") itemId: Long,
    ): Response<ResponseBody<EncyclopediaData>>

    @GET("/collection/collections")
    suspend fun getEncyclopediaAll(
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("filter") filter: String,
    ): Response<ResponseBody<Encyclopedia>>
}
