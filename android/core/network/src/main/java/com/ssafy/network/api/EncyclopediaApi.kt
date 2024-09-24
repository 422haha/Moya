package com.ssafy.network.api

import com.ssafy.model.encyclopediadetail.EncyclopediaDetail
import com.ssafy.model.encyclopedialist.EncyclopediaList
import com.ssafy.network.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EncyclopediaApi {

    @GET("/collections/{parkId}")
    fun getEncyclopedia(
        @Path("parkId") parkId: Long
    ): Response<ResponseBody<EncyclopediaList>>

    @GET("/collections/{itemId}")
    fun getEncyclopediaDetail(
        @Path("itemId") itemId: Long
    ): Response<ResponseBody<EncyclopediaDetail>>
}