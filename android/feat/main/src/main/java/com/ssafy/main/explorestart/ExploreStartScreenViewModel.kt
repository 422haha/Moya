package com.ssafy.main.explorestart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.ExplorationRepository
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreStartScreenViewModel @Inject constructor(
    private val explorationRepository: ExplorationRepository
) : ViewModel() {
    private val _state = MutableStateFlow<ExploreStartScreenState>(ExploreStartScreenState.Loading)
    val state: StateFlow<ExploreStartScreenState> = _state

    fun loadInitialData(parkId: Long) {
        viewModelScope.launch {
            explorationRepository.startExploration(parkId).collectLatest { response ->
                _state.value = when(response){
                    is ApiResponse.Success -> {
                        response.body?.let { body ->
                            ExploreStartScreenState.Loaded(
                                markerPositions = body.npcs.flatMap { it.positions }.map { LatLng(it.latitude, it.longitude) }
                            )
                        } ?: ExploreStartScreenState.Error("Failed to load initial data")
                    }
                    is ApiResponse.Error -> {
                        ExploreStartScreenState.Error(response.errorMessage?: "Unknown error")
                    }
                }
            }
        }
    }


    fun onIntent(intent: ExploreStartUserIntent) {

    }
}