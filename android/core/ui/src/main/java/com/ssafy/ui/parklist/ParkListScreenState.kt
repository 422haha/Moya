package com.ssafy.ui.parklist

import com.ssafy.ui.explorelist.BoxWithImageStateWithData

sealed interface ParkListScreenState {
    data object Loading : ParkListScreenState
    data class Loaded(val list: List<BoxWithImageStateWithData>) : ParkListScreenState
    data class Error(val message: String) : ParkListScreenState
}