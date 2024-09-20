package com.ssafy.ui.home

import com.ssafy.ui.component.UserIntent

sealed interface HomeUserIntent : UserIntent {
    data object OnNavigateToParkList : HomeUserIntent
    data object OnNavigateToExploreList : HomeUserIntent
}