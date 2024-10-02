package com.ssafy.main.encyclopedia

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.EncyclopediaRepository
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.encyclopedia.EncycScreenState
import com.ssafy.ui.encyclopedia.EncycUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncycScreenViewModel
    @Inject
    constructor(
        private val encyclopediaRepository: EncyclopediaRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<EncycScreenState>(EncycScreenState.Loading)
        val state: StateFlow<EncycScreenState> = _state

        fun loadInitialParkEncyclopedia(
            parkId: Long,
            page: Int,
            size: Int,
            filter: String,
        ) {
            viewModelScope.launch {
                encyclopediaRepository
                    .getEncyclopediaByParkId(parkId, page, size, filter)
                    .collectLatest { response ->
                        _state.value =
                            when (response) {
                                is ApiResponse.Success -> {
                                    response.body?.let { body ->
                                        EncycScreenState.Loaded(
                                            items =
                                                body.species.map {
                                                    EncycCardState(
                                                        id = it.speciesId,
                                                        name = it.speciesName,
                                                        imageUrl = it.imageUrl,
                                                        isDiscovered = it.discovered,
                                                    )
                                                },
                                            progress = body.progress.toFloat(),
                                        )
                                    } ?: EncycScreenState.Error("Failed to load initial data")
                                }

                                is ApiResponse.Error -> {
                                    EncycScreenState.Error(response.errorMessage ?: "Unknown error")
                                }
                            }
                    }
            }
        }

        fun loadInitialAllEncyclopedia(
            page: Int,
            size: Int,
            filter: String,
        ) {
            viewModelScope.launch {
                encyclopediaRepository
                    .getEncyclopediaAll(page, size, filter)
                    .collectLatest { response ->
                        _state.value =
                            when (response) {
                                is ApiResponse.Success -> {
                                    response.body?.let { body ->
                                        EncycScreenState.Loaded(
                                            items =
                                                body.species.map {
                                                    EncycCardState(
                                                        id = it.speciesId,
                                                        name = it.speciesName,
                                                        imageUrl = it.imageUrl,
                                                        isDiscovered = it.discovered,
                                                    )
                                                },
                                            progress = body.progress.toFloat(),
                                        )
                                    } ?: EncycScreenState.Error("Failed to load initial data")
                                }

                                is ApiResponse.Error -> {
                                    EncycScreenState.Error(response.errorMessage ?: "Unknown error")
                                }
                            }
                    }
            }
        }

        //TODO 여기도 parkId에 따라 데이터를 불러오도록 수정
        fun onIntent(intent: EncycUserIntent) {
            when (intent) {
                is EncycUserIntent.OnChipSelected -> {
                    val selectedChipIndex = intent.index
                    val filter =
                        when (selectedChipIndex) {
                            0 -> "all"
                            1 -> "completed"
                            2 -> "undiscovered"
                            else -> "all"
                        }

                    loadInitialAllEncyclopedia(page = 1, size = 10, filter = filter)
                }
            }
        }
    }
