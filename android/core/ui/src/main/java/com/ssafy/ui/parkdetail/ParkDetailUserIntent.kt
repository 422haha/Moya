package com.ssafy.ui.parkdetail

interface ParkDetailUserIntent {
    object OnPop : ParkDetailUserIntent

    object OnEnterExplore : ParkDetailUserIntent

    data class OnItemSelect(
        val id: Long,
    ) : ParkDetailUserIntent
}
