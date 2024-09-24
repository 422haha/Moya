package com.ssafy.ui.home

import androidx.compose.runtime.Immutable
import com.ssafy.ui.component.BoxWithImageState

sealed interface HomeScreenState {
    @Immutable
    data object Loading : HomeScreenState
    @Immutable
    data class Loaded(
        val userName: String,
        val userImage: String?,
        val exploreState: BoxWithImageState,
        val parkState: BoxWithImageState,
    ) : HomeScreenState
    @Immutable
    data class Error(
        val message: String
    ) : HomeScreenState
}