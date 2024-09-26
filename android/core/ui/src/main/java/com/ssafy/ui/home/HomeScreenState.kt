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
        val popularParks: List<ImageCardWithTitleDescriptionState>,
        val closeParks: List<ImageCardWithValueState>,
        val plantInSeason: List<EncycCardState>,
    ) : HomeScreenState
    @Immutable
    data class Error(
        val message: String
    ) : HomeScreenState
}