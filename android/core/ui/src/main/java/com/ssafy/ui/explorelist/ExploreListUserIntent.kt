package com.ssafy.ui.explorelist

import com.ssafy.ui.component.UserIntent

sealed interface ExploreListUserIntent : UserIntent {
    data object OnPop : ExploreListUserIntent

    data class OnItemSelect(
        val id: Long,
    ) : ExploreListUserIntent
}
