package com.ssafy.main.dialog

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.ui.explorestart.Missions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChallengeDialogViewModel
    @Inject
    constructor(
        private val explorationRepository: ExplorationRepository,
    ) : ViewModel() {
        private val _state = mutableStateOf<List<Missions>>(emptyList())
        val state: State<List<Missions>> = _state

        fun loadInitialData(explorationId: Long) {
            viewModelScope.launch {
                explorationRepository.getQuestList(explorationId).collectLatest { response ->
                    _state.value =
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    body.quest.map { quest ->
                                        Missions(
                                            id = quest.questId,
                                            missionTitle = "", // TODO 미션 타이틀 받아오기
                                            isSuccess = quest.completed == "WAIT",
                                        )
                                    }
                                } ?: emptyList()
                            }

                            is ApiResponse.Error -> {
                                emptyList()
                            }
                        }
                }
            }
        }
    }
