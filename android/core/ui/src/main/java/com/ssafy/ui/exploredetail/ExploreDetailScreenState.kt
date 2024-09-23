package com.ssafy.ui.exploredetail

import com.ssafy.ui.encyclopedia.EncycGridState

sealed interface ExploreDetailScreenState {
    data object Loading : ExploreDetailScreenState
    data class Loaded(
        val exploreDetail: ExploreDetail,
        val items: List<EncycGridState> = listOf()
    ) : ExploreDetailScreenState

    data class Error(val message: String) : ExploreDetailScreenState
}