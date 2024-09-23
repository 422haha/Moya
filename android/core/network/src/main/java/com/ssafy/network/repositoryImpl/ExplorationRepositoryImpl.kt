package com.ssafy.network.repositoryImpl

import com.ssafy.model.CompletedQuest
import com.ssafy.model.ExplorationEndData
import com.ssafy.model.ExplorationInitialData
import com.ssafy.model.ExplorationJournalRecent
import com.ssafy.model.QuestList
import com.ssafy.model.SpeciesMinimumInfo
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.ExplorationApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.network.request.ExplorationEndRequestBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExplorationRepositoryImpl @Inject constructor(
    private val explorationApi: ExplorationApi,
) : ExplorationRepository {
    override suspend fun startExploration(parkId: Long): Flow<ApiResponse<ExplorationInitialData>> {
        return flow {
            val response =
                apiHandler {
                    explorationApi.startExploration(parkId)
                }
            when (response) {
                is ApiResponse.Success -> {
                    emit(ApiResponse.Success(response.body?.data))
                }

                is ApiResponse.Error -> {
                    emit(
                        ApiResponse.Error(
                            errorCode = response.errorCode,
                            errorMessage = response.errorMessage,
                        ),
                    )
                }
            }
        }
    }

    override suspend fun getRecentExploration(
        explorationId: Long,
        body: ExplorationEndRequestBody
    ): Flow<ApiResponse<ExplorationJournalRecent>> {
        TODO("Not yet implemented")
    }

    override suspend fun endExploration(
        explorationId: Long,
        body: ExplorationEndRequestBody
    ): Flow<ApiResponse<ExplorationEndData>> {
        TODO("Not yet implemented")
    }

    override suspend fun registerSpecies(
        explorationId: Long,
        body: ExplorationEndRequestBody
    ): Flow<ApiResponse<SpeciesMinimumInfo>> {
        TODO("Not yet implemented")
    }

    override suspend fun getExplorationData(explorationId: Long): Flow<ApiResponse<ExplorationEndData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getQuestList(explorationId: Long): Flow<ApiResponse<QuestList>> {
        TODO("Not yet implemented")
    }

    override suspend fun completeQuest(
        explorationId: Long,
        questId: Long
    ): Flow<ApiResponse<CompletedQuest>> {
        TODO("Not yet implemented")
    }

}
