package com.ssafy.network.repositoryImpl

import com.ssafy.model.CompletedQuest
import com.ssafy.model.ExplorationData
import com.ssafy.model.ExplorationEndData
import com.ssafy.model.ExplorationInitialData
import com.ssafy.model.QuestList
import com.ssafy.model.SpeciesMinimumInfo
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.ExplorationApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.network.request.ExplorationEndRequestBody
import com.ssafy.network.request.RegisterSpeciesRequestBody
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

    override suspend fun endExploration(
        explorationId: Long,
        body: ExplorationEndRequestBody
    ): Flow<ApiResponse<ExplorationEndData>> {
        return flow {
            val response =
                apiHandler {
                    explorationApi.endExploration(explorationId, body)
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

    override suspend fun registerSpecies(
        explorationId: Long,
        body: RegisterSpeciesRequestBody
    ): Flow<ApiResponse<SpeciesMinimumInfo>> {
        return flow {
            val response =
                apiHandler {
                    explorationApi.registerSpecies(explorationId, body)
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

    override suspend fun getExplorationData(
        parkId: Long,
        explorationId: Long
    ): Flow<ApiResponse<ExplorationData>> {
        return flow {
            val response =
                apiHandler {
                    explorationApi.getExplorationData(parkId = parkId, explorationId = explorationId)
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

    override suspend fun getQuestList(explorationId: Long): Flow<ApiResponse<QuestList>> {
        return flow {
            val response =
                apiHandler {
                    explorationApi.getQuestList(explorationId)
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

    override suspend fun completeQuest(
        explorationId: Long,
        questId: Long
    ): Flow<ApiResponse<CompletedQuest>> {
        return flow {
            val response =
                apiHandler {
                    explorationApi.completeQuest(explorationId, questId)
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

}
