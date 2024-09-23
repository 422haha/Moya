package com.ssafy.ui.explorestart

import com.naver.maps.geometry.LatLng

sealed interface ExploreStartScreenState {
    data object Loading : ExploreStartScreenState
    data class Loaded(
        val markerPositions: List<LatLng> = listOf(),
        val showExitDialog: Boolean = false,
        val showChallengeDialog: Boolean = false
    ) : ExploreStartScreenState

    data class Error(val message: String) : ExploreStartScreenState
}

data class Missions(
    val missionTitle: String,
    val isSuccess: Boolean
)