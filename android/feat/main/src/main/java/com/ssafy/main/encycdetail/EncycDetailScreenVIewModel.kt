package com.ssafy.main.encycdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.EncyclopediaRepository
import com.ssafy.ui.encycdetail.EncycDetail
import com.ssafy.ui.encycdetail.EncycDetailScreenState
import com.ssafy.ui.encycdetail.EncycDetailUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EncycDetailScreenVIewModel
    @Inject
    constructor(
        private val encyclopediaRepository: EncyclopediaRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<EncycDetailScreenState>(EncycDetailScreenState.Loading)
        val state: StateFlow<EncycDetailScreenState> = _state

        fun loadInitialData(itemId: Long) {
            viewModelScope.launch {
                encyclopediaRepository.getEncyclopediaDetail(itemId).collectLatest { response ->
                    _state.value =
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    EncycDetailScreenState.Loaded(
                                        data =
                                            EncycDetail(
                                                plantId = body.itemId,
                                                plantName = body.speciesName,
                                                plantImage = body.imageUrl,
                                                description = body.description,
                                                userPhoto = body.userPhotos.getOrNull(0)?.imageUrl,
                                            ),
                                    )
                                } ?: EncycDetailScreenState.Error("Failed to load initial data")
                            }

                            is ApiResponse.Error -> {
                                EncycDetailScreenState.Error(
                                    response.errorMessage ?: "Unknown error",
                                )
                            }
                        }
                }
            }
        }

        fun onIntent(intent: EncycDetailUserIntent) {
            when (intent) {
            }
        }
    }
