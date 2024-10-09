package com.ssafy.main.parklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.model.LatLng
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ParkRepository
import com.ssafy.ui.component.ImageCardWithValueState
import com.ssafy.ui.formatDistance
import com.ssafy.ui.parklist.ParkListScreenState
import com.ssafy.ui.parklist.ParkListUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParkListScreenViewModel
    @Inject
    constructor(
        private val parkRepository: ParkRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<ParkListScreenState>(ParkListScreenState.Loading)
        val state: StateFlow<ParkListScreenState> = _state

        fun loadInitialData(
            latitude: Double,
            longitude: Double,
        ) {
            viewModelScope.launch {
                parkRepository
                    .getParkList(
                        page = 1,
                        size = 10,
                        latitude = latitude,
                        longitude = longitude,
                    ).collectLatest { response ->
                        _state.value =
                            when (response) {
                                is ApiResponse.Success -> {
                                    response.body?.let { body ->
                                        ParkListScreenState.Loaded(
                                            location = LatLng(latitude, longitude),
                                            page = 1,
                                            list =
                                                body.parks.map { park ->
                                                    ImageCardWithValueState(
                                                        id = park.parkId,
                                                        title = park.parkName,
                                                        value = park.distance.toString().formatDistance(),
                                                        imageUrl = park.imageUrl,
                                                    )
                                                },
                                        )
                                    } ?: ParkListScreenState.Error("Failed to load initial data")
                                }

                                is ApiResponse.Error -> {
                                    ParkListScreenState.Error(response.errorMessage ?: "Unknown error")
                                }
                            }
                    }
            }
        }

        fun onIntent(intent: ParkListUserIntent) {
            when (intent) {
                is ParkListUserIntent.OnLoadPage -> loadNextPage()
                else -> {}
            }
        }

        private fun loadNextPage() {
            viewModelScope.launch {
                if (state.value is ParkListScreenState.Loaded) {
                    val state = state.value as ParkListScreenState.Loaded
                    parkRepository
                        .getParkList(state.page + 1, 10, state.location.latitude, state.location.longitude)
                        .collectLatest { response ->
                            _state.value =
                                when (response) {
                                    is ApiResponse.Success -> {
                                        response.body?.let { body ->
                                            state.copy(
                                                page = state.page + 1,
                                                list =
                                                    state.list.plus(
                                                        body.parks.map { park ->
                                                            ImageCardWithValueState(
                                                                id = park.parkId,
                                                                title = park.parkName,
                                                                value =
                                                                    park.distance
                                                                        .toString()
                                                                        .formatDistance(),
                                                                imageUrl = park.imageUrl,
                                                            )
                                                        },
                                                    ),
                                            )
                                        } ?: ParkListScreenState.Error("Failed to load initial data")
                                    }

                                    is ApiResponse.Error -> {
                                        ParkListScreenState.Error(
                                            response.errorMessage ?: "Unknown error",
                                        )
                                    }
                                }
                        }
                }
            }
        }
    }
