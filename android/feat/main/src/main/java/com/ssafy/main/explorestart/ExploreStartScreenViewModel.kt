package com.ssafy.main.explorestart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.network.request.ExplorationEndRequestBody
import com.ssafy.ui.explorestart.ExploreStartDialogState
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreStartScreenViewModel
    @Inject
    constructor(
        private val explorationRepository: ExplorationRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<ExploreStartScreenState>(ExploreStartScreenState.Loading)
        val state: StateFlow<ExploreStartScreenState> = _state

        private val _dialogState =
            MutableStateFlow<ExploreStartDialogState>(ExploreStartDialogState.Closed)
        val dialogState: StateFlow<ExploreStartDialogState> = _dialogState

        fun loadInitialData(parkId: Long) {
            viewModelScope.launch {
                explorationRepository.startExploration(parkId).collectLatest { response ->
                    _state.value =
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    ExploreStartScreenState.Loaded(
                                        explorationId = body.id,
                                        npcPositions =
                                            body.npcs
                                                .flatMap { it.positions }
                                                .map { LatLng(it.latitude, it.longitude) },
                                        discoveredPositions =
                                            body.myDiscoveredSpecies
                                                .flatMap { it.positions }
                                                .map { LatLng(it.latitude, it.longitude) },
                                        speciesPositions =
                                            body.species
                                                .flatMap { it.positions }
                                                .map { LatLng(it.latitude, it.longitude) },
                                    )
                                } ?: ExploreStartScreenState.Error("Failed to load initial data")
                            }

                            is ApiResponse.Error -> {
                                ExploreStartScreenState.Error(response.errorMessage ?: "Unknown error")
                            }
                        }
                }
            }
        }

        fun onIntent(intent: ExploreStartUserIntent) {
            when (intent) {
                is ExploreStartUserIntent.OnDialogDismissed -> {
                    _dialogState.value = ExploreStartDialogState.Closed
                }

                is ExploreStartUserIntent.OnExitClicked -> { // 탐험 종료 버튼
                    _dialogState.value = ExploreStartDialogState.Exit
                }

                is ExploreStartUserIntent.OnExitExplorationConfirmed -> endExploration(onEnd = intent.onExit)

                is ExploreStartUserIntent.OnOpenChallengeList -> {
                    _dialogState.value = ExploreStartDialogState.Challenge
                }
            }
        }

        private fun endExploration(onEnd: () -> Unit = {}) {
            viewModelScope.launch {
                if (state.value is ExploreStartScreenState.Loaded) {
                    val uiState = state.value as ExploreStartScreenState.Loaded

                    explorationRepository
                        .endExploration(
                            uiState.explorationId,
                            body =
                                ExplorationEndRequestBody(
                                    route = emptyList(), // TODO : 이동경로 저장
                                    steps = 0,
                                ),
                        ).collectLatest { response ->
                            Log.d("TAG", "endExploration: $response")
                            onEnd()
                        }
                }
            }
        }
    }
