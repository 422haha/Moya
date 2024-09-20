package com.ssafy.ui.explorestart

import com.naver.maps.geometry.LatLng

sealed interface ExploreStartScreenState {
    data object Loading : ExploreStartScreenState
    data class Loaded(
        val markerPositions: List<LatLng> = listOf()
    ) : ExploreStartScreenState

    data class Error(val message: String) : ExploreStartScreenState
    data class ShowExitDialog(val isVisible: Boolean) : ExploreStartScreenState
    data class ShowChallengeDialog(
        val isVisible: Boolean,
        val missions: List<Missions> = listOf()
    ) : ExploreStartScreenState
}

data class Missions(
    val missionTitle: String,
    val isSuccess: Boolean
)