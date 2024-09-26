package com.ssafy.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ParkRepository
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.component.ImageCardWithTitleDescriptionState
import com.ssafy.ui.component.ImageCardWithValueState
import com.ssafy.ui.home.HomeScreenState
import com.ssafy.ui.home.HomeUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel
    @Inject
    constructor(
        private val parkRepository: ParkRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<HomeScreenState>(HomeScreenState.Loading)
        val state: StateFlow<HomeScreenState> = _state

        fun loadInitialData(latitude: Double, longitude: Double) {
            _state.value =
                HomeScreenState.Loaded(
                    popularParks =
                        List(5) {
                            ImageCardWithTitleDescriptionState(
                                id = 1,
                                title = "동락공원",
                                description = "동락공원은 동락동에 위치한 공원입니다.",
                            )
                        },
                    closeParks =
                        List(3) {
                            ImageCardWithValueState(
                                id = 1,
                                title = "동락공원",
                                value = "99m",
                            )
                        },
                    plantInSeason =
                        List(3) {
                            EncycCardState(
                                id = 1,
                                name = "무궁화",
                                imageUrl = "",
                                isDiscovered = true,
                            )
                        },
                )

            viewModelScope.launch {
                parkRepository
                    .getParkList(1, 3, longitude, latitude)
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Success -> {
                                response.body?.let { body ->
                                    val parks =
                                        body.parks.map {
                                            ImageCardWithValueState(
                                                id = it.parkId,
                                                title = it.parkName,
                                                value = it.distance.toString(),
                                            )
                                        }

                                    if (state.value is HomeScreenState.Loaded) {
                                        _state.emit(
                                            (state.value as HomeScreenState.Loaded).copy(
                                                closeParks = parks,
                                            ),
                                        )
                                    } else {
                                        _state.emit(
                                            HomeScreenState.Loaded(
                                                closeParks = parks,
                                            ),
                                        )
                                    }
                                }
                            }

                            is ApiResponse.Error -> {
                            }
                        }
                    }
            }
        }

        fun onIntent(intent: HomeUserIntent) {
        }
    }
