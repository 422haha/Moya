package com.ssafy.ui.explorestart

import androidx.compose.runtime.Immutable
import com.naver.maps.geometry.LatLng

sealed interface ExploreStartScreenState {
    @Immutable
    data object Loading : ExploreStartScreenState

    @Immutable
    data class Loaded(
        val npcPositions: List<LatLng> = listOf(),
        val discoveredPositions: List<LatLng> = listOf(),
        val speciesPositions: List<LatLng> = listOf(),
        val showExitDialog: Boolean = false,
        val showChallengeDialog: Boolean = false,
    ) : ExploreStartScreenState

    @Immutable
    data class Error(
        val message: String,
    ) : ExploreStartScreenState
}

@Immutable
data class Missions(
    val id: Long,
    val missionTitle: String,
    val isSuccess: Boolean,
)
