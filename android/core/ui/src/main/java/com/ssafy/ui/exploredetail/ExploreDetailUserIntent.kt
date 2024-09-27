package com.ssafy.ui.exploredetail

interface ExploreDetailUserIntent {
    data object OnPop : ExploreDetailUserIntent
    data class OnEncycItemClicked(val encycId: Long) : ExploreDetailUserIntent
}