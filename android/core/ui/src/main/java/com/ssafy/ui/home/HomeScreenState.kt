package com.ssafy.ui.home

import com.ssafy.ui.component.BoxWithImageState

sealed interface HomeScreenState {
    data object Loading : HomeScreenState
    data class Loaded(
        val userName: String,
        val userImage: String?,
        val exploreState: BoxWithImageState,
        val parkState: BoxWithImageState,
    ) : HomeScreenState
    data class Error(
        val message: String
    ) : HomeScreenState
}