package com.ssafy.moya

import kotlinx.serialization.Serializable

sealed interface BottomNavigationRoute {
    @Serializable
    data object Home : BottomNavigationRoute

    @Serializable
    data object Encyc : BottomNavigationRoute

    @Serializable
    data object ExploreJournal : BottomNavigationRoute
}
