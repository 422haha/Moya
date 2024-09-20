package com.ssafy.network.api

import com.ssafy.model.ExplorationData
import com.ssafy.network.ResponseBody
import com.ssafy.network.request.ExplorationEndRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExplorationApi {
    @GET("/exploration/start/{parkId}")
    fun startExploration(
        @Path("parkId") parkId: Long,
    ): Response<ResponseBody<ExplorationData>>

    @POST("/explorations/{explorationId}/end")
    fun endExploration(
        @Path("explorationId") explorationId: Long,
        @Body body: ExplorationEndRequestBody,
    ): Response<ResponseBody<String>>

    @POST("/explorations/{explorationId}/camera")
    fun registerSpecies(
        @Path("explorationId") explorationId: Long,
        @Body body: ExplorationEndRequestBody,
    ): Response<ResponseBody<String>>

    @GET("/explorations/{explorationId}/quest/list")
    fun getQuestList(
        @Path("explorationId") explorationId: Long,
    )
}
