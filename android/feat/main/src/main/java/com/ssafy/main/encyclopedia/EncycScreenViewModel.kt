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

        fun loadInitialData(parkId: Long) {
            viewModelScope.launch {
                encyclopediaRepository.getEncyclopedia(parkId).collectLatest { response ->
                    _state.value =
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    EncycScreenState.Loaded(
                                        items =
                                            body.data.species.map {
                                                EncycCardState(
                                                    id = it.speciesId,
                                                    name = it.speciesName,
                                                    imageUrl = it.imageUrl,
                                                    isDiscovered = it.discovered,
                                                )
                                            },
                                        progress = body.data.progress.toFloat(),
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

        fun onIntent(intent: EncycUserIntent) {
        }
    }
