package com.ssafy.ui.explorestart

interface ExploreStartUserIntent {
    data object OnExitExploreConfirmed : ExploreStartUserIntent
    data object OnExitExploreRequested : ExploreStartUserIntent // 탐험을 종료할까요 다이얼로그 띄워줌
    data object OnExitExploreDismissed : ExploreStartUserIntent
    data object OnChallengeConfirmed : ExploreStartUserIntent
    data object OnChallengeDismissed : ExploreStartUserIntent
    data object OnEnterEncyc : ExploreStartUserIntent
    data object OnCameraClicked : ExploreStartUserIntent
}