package com.ssafy.network.repositoryImpl

import com.ssafy.model.Park
import com.ssafy.model.SpeciesInSeason
import com.ssafy.network.ApiResponse
import com.ssafy.network.api.SeasonApi
import com.ssafy.network.apiHandler
import com.ssafy.network.repository.SeasonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SeasonRepositoryImpl @Inject constructor(
    private val seasonApi: SeasonApi
) : SeasonRepository {
    override suspend fun getSpeciesInSeason(): Flow<ApiResponse<List<SpeciesInSeason>>> {
        return flow{
            val response =
                apiHandler {
                    seasonApi.getSpeciesInSeason()
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

    override suspend fun getParksBySpeciesId(speciesId: Long): Flow<ApiResponse<List<Park>>> {
        return flow{
            val response =
                apiHandler {
                    seasonApi.getParksBySpeciesId(speciesId)
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