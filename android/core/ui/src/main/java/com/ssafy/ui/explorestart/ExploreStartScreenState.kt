package com.ssafy.ui.explorestart

import androidx.compose.runtime.Immutable
import com.naver.maps.geometry.LatLng

sealed interface ExploreStartScreenState {
    @Immutable
    data object Loading : ExploreStartScreenState
    @Immutable
    data class Loaded(
        val markerPositions: List<LatLng> = listOf(),
        val showExitDialog: Boolean = false,
        val showChallengeDialog: Boolean = false
    ) : ExploreStartScreenState
    @Immutable
    data class Error(val message: String) : ExploreStartScreenState
}

data class Missions(
    val missionTitle: String,
    val isSuccess: Boolean
)