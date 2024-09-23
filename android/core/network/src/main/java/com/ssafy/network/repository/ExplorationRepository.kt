package com.ssafy.network.repository

import com.ssafy.model.CompletedQuest
import com.ssafy.model.ExplorationEndData
import com.ssafy.model.ExplorationInitialData
import com.ssafy.model.ExplorationJournalRecent
import com.ssafy.model.QuestList
import com.ssafy.model.SpeciesMinimumInfo
import com.ssafy.network.ApiResponse
import com.ssafy.network.request.ExplorationEndRequestBody
import kotlinx.coroutines.flow.Flow

interface ExplorationRepository {
    suspend fun startExploration(parkId: Long): Flow<ApiResponse<ExplorationInitialData>>

    suspend fun getRecentExploration(
        explorationId: Long,
        body: ExplorationEndRequestBody,
    ): Flow<ApiResponse<ExplorationJournalRecent>>

    suspend fun endExploration(
        explorationId: Long,
        body: ExplorationEndRequestBody,
    ): Flow<ApiResponse<ExplorationEndData>>

    suspend fun registerSpecies(
        explorationId: Long,
        body: ExplorationEndRequestBody,
    ): Flow<ApiResponse<SpeciesMinimumInfo>>

    suspend fun getExplorationData(explorationId: Long): Flow<ApiResponse<ExplorationEndData>>

    suspend fun getQuestList(explorationId: Long): Flow<ApiResponse<QuestList>>

    suspend fun completeQuest(
        explorationId: Long,
        questId: Long,
    ): Flow<ApiResponse<CompletedQuest>>
}
