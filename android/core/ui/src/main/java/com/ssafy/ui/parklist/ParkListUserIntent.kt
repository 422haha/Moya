package com.ssafy.ui.parklist

sealed interface ParkListUserIntent {
    data object OnPop : ParkListUserIntent

    data class OnItemSelect(
        val id: Long,
    ) : ParkListUserIntent
}
