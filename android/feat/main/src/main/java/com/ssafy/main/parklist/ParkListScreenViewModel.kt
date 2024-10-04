package com.ssafy.main.parklist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ParkListScreenViewModel @Inject constructor(
    private val parkRepository: ParkRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ParkListScreenState>(ParkListScreenState.Loading)
    val state: StateFlow<ParkListScreenState> = _state

    fun loadInitialData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            parkRepository.getParkList(
                page = 1,
                size = 10,
                latitude = latitude,
                longitude = longitude,
            ).collectLatest { response ->
                _state.value = when(response){
                    is ApiResponse.Success -> {
                        response.body?.let { body ->
                            ParkListScreenState.Loaded(
                                list = body.parks.map { park ->
                                    ImageCardWithValueState(
                                        id = park.parkId,
                                        title = park.parkName,
                                        value = park.distance.toString().formatDistance(),
                                        imageUrl = park.imageUrl,
                                    )
                                }
                            )
                        } ?: ParkListScreenState.Error("Failed to load initial data")
                    }
                    is ApiResponse.Error -> {
                        Log.d("TAG", "loadInitialData: ${response.errorMessage}")
                        ParkListScreenState.Error(response.errorMessage?: "Unknown error")
                    }
                }

            }
        }
    }

    fun onIntent(intent: ParkListUserIntent) {

    }
}