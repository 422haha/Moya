package com.ssafy.ui.exploredetail

import androidx.compose.runtime.Immutable
import com.ssafy.ui.encyclopedia.EncycGridState

sealed interface ExploreDetailScreenState {
    @Immutable
    data object Loading : ExploreDetailScreenState
    @Immutable
    data class Loaded(
        val exploreDetail: ExploreDetail,
        val items: List<EncycGridState> = listOf()
    ) : ExploreDetailScreenState
    @Immutable
    data class Error(val message: String) : ExploreDetailScreenState
}