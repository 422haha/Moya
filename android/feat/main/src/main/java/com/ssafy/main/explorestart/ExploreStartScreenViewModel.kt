package com.ssafy.main.explorestart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.geometry.LatLng
import com.ssafy.ui.explorestart.ExploreStartScreenState
import com.ssafy.ui.explorestart.ExploreStartUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreStartScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<ExploreStartScreenState>(ExploreStartScreenState.Loading)
    val state: StateFlow<ExploreStartScreenState> = _state

    init {
        viewModelScope.launch {
            val markerPositions = listOf(
                LatLng(37.5665, 126.9780),
                LatLng(35.1796, 129.0756)
            )
            _state.value = ExploreStartScreenState.Loaded(markerPositions = markerPositions)
        }
    }

    fun onIntent(intent: ExploreStartUserIntent) {

    }
}