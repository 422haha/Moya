package com.ssafy.network.api

import com.ssafy.model.CompletedQuest
import com.ssafy.model.ExplorationData
import com.ssafy.model.ExplorationInitialData
import com.ssafy.model.ExplorationJournalRecent
import com.ssafy.model.QuestList
import com.ssafy.network.ResponseBody
import com.ssafy.network.request.ExplorationEndRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExplorationApi {

    @GET("/exploration/home")
    fun getRecentExploration(): Response<ResponseBody<ExplorationJournalRecent>>

    @GET("/exploration/start/{parkId}")
    fun startExploration(
        @Path("parkId") parkId: Long,
    ): Response<ResponseBody<ExplorationInitialData>>

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
    ): Response<ResponseBody<QuestList>>

    @POST("/explorations/{explorationId}/quest/{questId}/complete")
    fun completeQuest(
        @Path("explorationId") explorationId: Long,
        @Path("questId") questId: Long,
    ): Response<ResponseBody<CompletedQuest>>

    @GET("/explorations/load/{explorationId}")
    fun getExplorationData(
        @Path("explorationId") explorationId: Long,
    ): Response<ResponseBody<ExplorationData>>
}
