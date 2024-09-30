package com.ssafy.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ParkRepository
import com.ssafy.network.repository.SeasonRepository
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.component.ImageCardWithTitleDescriptionState
import com.ssafy.ui.component.ImageCardWithValueState
import com.ssafy.ui.home.HomeScreenState
import com.ssafy.ui.home.HomeUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
    @Inject
    constructor(
        private val parkRepository: ParkRepository,
        private val seasonRepository: SeasonRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
        val state: StateFlow<HomeScreenState> = _state

        fun loadInitialData(
            latitude: Double,
            longitude: Double,
        ) {
            viewModelScope.launch {
                combine(
                    parkRepository.getFamousParks(latitude, longitude),
                    parkRepository.getParkList(1, 3, latitude, longitude),
                    seasonRepository.getSpeciesInSeason(),
                ) { famous, close, season ->

                    if (famous is ApiResponse.Error) {
                        return@combine HomeScreenState.Error(
                            famous.errorMessage ?: "",
                        )
                    }
                    if (close is ApiResponse.Error) {
                        return@combine HomeScreenState.Error(
                            close.errorMessage ?: "",
                        )
                    }
                    if (season is ApiResponse.Error) {
                        return@combine HomeScreenState.Error(
                            season.errorMessage ?: "",
                        )
                    }

                    return@combine HomeScreenState.Loaded(
                        popularParks =
                            (famous as ApiResponse.Success).body?.map {
                                ImageCardWithTitleDescriptionState(
                                    id = it.parkId,
                                    title = it.parkName,
                                    description = it.distance.toString(),
                                )
                            } ?: emptyList(),
                        closeParks =
                            (close as ApiResponse.Success).body?.parks?.map {
                                ImageCardWithValueState(
                                    id = it.parkId,
                                    title = it.parkName,
                                    value = it.distance.toString(),
                                    imageUrl = it.imageUrl,
                                )
                            } ?: emptyList(),
                        plantInSeason =
                            (season as ApiResponse.Success).body?.map {
                                EncycCardState(
                                    id = it.speciesId,
                                    name = it.name,
                                    imageUrl = it.imageUrl,
                                    isDiscovered = false,
                                )
                            } ?: emptyList(),
                    )
                }.collect { state ->
                    _state.value = state
                }
            }
        }

        fun onIntent(intent: HomeUserIntent) {
        }
    }
