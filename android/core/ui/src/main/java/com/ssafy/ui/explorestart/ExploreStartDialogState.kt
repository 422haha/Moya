package com.ssafy.ui.explorestart

sealed interface ExploreStartDialogState {
    data object Closed : ExploreStartDialogState

    data object Exit : ExploreStartDialogState

    data object Challenge : ExploreStartDialogState
}
