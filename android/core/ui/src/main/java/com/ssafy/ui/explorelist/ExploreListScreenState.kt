package com.ssafy.ui.explorelist

import androidx.compose.runtime.Immutable
import com.ssafy.ui.component.BoxWithImageState
import com.ssafy.ui.component.UserIntent
import com.ssafy.ui.exploredetail.ExploreDetail

sealed interface ExploreListScreenState : UserIntent {
    @Immutable
    data object Loading : ExploreListScreenState

    @Immutable
    data class Loaded(
        val userName: String,
        val list: List<ExploreDetail>,
    ) : ExploreListScreenState

    @Immutable
    data class Error(
        val message: String,
    ) : ExploreListScreenState
}

data class BoxWithImageStateWithData(
    val id: Long,
    val state: BoxWithImageState,
)
