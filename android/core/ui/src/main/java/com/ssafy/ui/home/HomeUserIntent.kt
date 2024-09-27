package com.ssafy.ui.home

import com.ssafy.ui.component.UserIntent

sealed interface HomeUserIntent : UserIntent {
    data object OnNavigateToParkList : HomeUserIntent

    data class OnSelectPopularPark(
        val id: Long,
    ) : HomeUserIntent

    data class OnSelectClosePark(
        val id: Long,
    ) : HomeUserIntent

    data class OnSelectEncyc(
        val id: Long,
    ) : HomeUserIntent
}
