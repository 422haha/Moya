package com.ssafy.ui.home

import androidx.compose.runtime.Immutable
import com.ssafy.ui.component.EncycCardState
import com.ssafy.ui.component.ImageCardWithTitleDescriptionState
import com.ssafy.ui.component.ImageCardWithValueState

sealed interface HomeScreenState {
    @Immutable
    data object Loading : HomeScreenState

    @Immutable
    data class Loaded(
        val popularParks: List<ImageCardWithTitleDescriptionState> = emptyList(),
        val closeParks: List<ImageCardWithValueState> = emptyList(),
        val plantInSeason: List<EncycCardState> = emptyList(),
    ) : HomeScreenState

    @Immutable
    data class Error(
        val message: String,
    ) : HomeScreenState
}
