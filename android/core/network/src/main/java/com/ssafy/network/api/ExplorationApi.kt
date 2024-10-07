package com.ssafy.network.api

import com.ssafy.model.Chatting
import com.ssafy.model.CompletedQuest
import com.ssafy.model.ExplorationData
import com.ssafy.model.ExplorationEndData
import com.ssafy.model.ExplorationInitialData
import com.ssafy.model.QuestList
import com.ssafy.model.SpeciesMinimumInfo
import com.ssafy.network.ResponseBody
import com.ssafy.network.request.ExplorationEndRequestBody
import com.ssafy.network.request.RegisterSpeciesRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExplorationApi {

    @GET("/exploration/start/{parkId}")
    suspend fun startExploration(
        @Path("parkId") parkId: Long,
    ): Response<ResponseBody<ExplorationInitialData>>

    @GET("/explorations/{parkId}/load/{explorationId}")
    suspend fun getExplorationData(
        @Path("parkId") parkId: Long,
        @Path("explorationId") explorationId: Long,
    ): Response<ResponseBody<ExplorationData>>

    @GET("/exploration/{explorationId}/quest/list")
    suspend fun getQuestList(
        @Path("explorationId") explorationId: Long,
    ): Response<ResponseBody<QuestList>>

    @POST("/exploration/{explorationId}/quest/{questId}/complete")
    suspend fun completeQuest(
        @Path("explorationId") explorationId: Long,
        @Path("questId") questId: Long,
    ): Response<ResponseBody<CompletedQuest>>

    @POST("/explorations/{explorationId}/end")
    suspend fun endExploration(
        @Path("explorationId") explorationId: Long,
        @Body body: ExplorationEndRequestBody,
    ): Response<ResponseBody<ExplorationEndData>>

    @POST("/exploration/{explorationId}/camera")
    suspend fun registerSpecies(
        @Path("explorationId") explorationId: Long,
        @Body body: RegisterSpeciesRequestBody,
    ): Response<ResponseBody<SpeciesMinimumInfo>>

    @POST("/exploration/{explorationId}/chat/{npcPosId}")
    suspend fun chattingNPC(
        @Path("explorationId") explorationId: Long,
        @Path("npcPosId") npcPosId: Long,
        @Body body: String,
    ): Response<ResponseBody<Chatting>>
}
