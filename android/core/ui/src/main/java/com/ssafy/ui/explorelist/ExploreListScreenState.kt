package com.ssafy.ui.explorelist

import com.ssafy.ui.component.BoxWithImageState
import com.ssafy.ui.component.UserIntent

sealed interface ExploreListScreenState : UserIntent {
    data object Loading : ExploreListScreenState
    data class Loaded(
        val list: List<BoxWithImageStateWithData>
    ) : ExploreListScreenState
    data class Error(
        val message: String
    ) : ExploreListScreenState
}

data class BoxWithImageStateWithData(
    val id: Long,
    val state: BoxWithImageState
)

