package com.ssafy.moya.navigation

import kotlinx.serialization.Serializable

sealed interface MainBottomNavigationRoute {
    @Serializable
    data object Home : MainBottomNavigationRoute

    @Serializable
    data object Encyc : MainBottomNavigationRoute

    @Serializable
    data object ExploreJournal : MainBottomNavigationRoute
}
