package com.ssafy.ui.parkdetail

interface ParkDetailScreenUserIntent {
    object OnPop : ParkDetailScreenUserIntent
    object OnEnterExplore : ParkDetailScreenUserIntent
    data class OnItemSelect(val id: Long) : ParkDetailScreenUserIntent
}