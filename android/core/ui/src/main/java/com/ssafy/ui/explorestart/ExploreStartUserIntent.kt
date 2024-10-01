package com.ssafy.ui.explorestart

interface ExploreStartUserIntent {
    data class OnExitExplorationConfirmed(
        val onExit: () -> Unit = {},
    ) : ExploreStartUserIntent

    data class OnChallengeSelected(
        val id: Long,
    ) : ExploreStartUserIntent

    data object OnDialogDismissed : ExploreStartUserIntent

    data object OnExitClicked : ExploreStartUserIntent

    data object OnOpenChallengeList : ExploreStartUserIntent

    data object OnEnterEncyc : ExploreStartUserIntent

    data object OnCameraClicked : ExploreStartUserIntent
}
