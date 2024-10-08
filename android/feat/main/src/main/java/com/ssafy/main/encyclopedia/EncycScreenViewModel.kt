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

        fun loadInitialParkEncyclopedia(parkId: Long) {
            if (parkId > 0) {
                loadFromId(parkId, 1, 0)
            } else {
                loadInitialAllEncyclopedia(1, 0)
            }
        }

        fun onIntent(
            parkId: Long,
            intent: EncycUserIntent,
        ) {
            when (intent) {
                is EncycUserIntent.OnChipSelected -> {
                    val page =
                        if (state.value is EncycScreenState.Loaded) (state.value as EncycScreenState.Loaded).page else 0

                    val chipIdx = intent.index

                    if (parkId > 0) {
                        loadFromId(parkId, page, chipIdx)
                    } else {
                        loadInitialAllEncyclopedia(page, chipIdx)
                    }
                }
            }
        }

        private fun loadFromId(
            parkId: Long,
            page: Int,
            chipIndex: Int,
        ) {
            viewModelScope.launch {
                encyclopediaRepository
                    .getEncyclopediaByParkId(parkId, page, 10, fromChipIndexToFilter(chipIndex))
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
                                            selectedChipIndex = chipIndex,
                                        )
                                    } ?: EncycScreenState.Error(
                                        "Failed to load initial data",
                                    )
                                }

                                is ApiResponse.Error -> {
                                    EncycScreenState.Error(
                                        response.errorMessage ?: "Unknown error",
                                    )
                                }
                            }
                    }
            }
        }

        private fun loadInitialAllEncyclopedia(
            page: Int,
            chipIndex: Int,
        ) {
            viewModelScope.launch {
                encyclopediaRepository
                    .getEncyclopediaAll(page, 10, fromChipIndexToFilter(chipIndex))
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
                                            selectedChipIndex = chipIndex,
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

        private fun fromChipIndexToFilter(index: Int): String =
            when (index) {
                0 -> "all"
                1 -> "completed"
                2 -> "undiscovered"
                else -> "all"
            }
    }
