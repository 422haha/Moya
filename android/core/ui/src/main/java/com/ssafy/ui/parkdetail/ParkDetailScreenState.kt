package com.ssafy.ui.parkdetail

import com.ssafy.ui.encyclopedia.EncycGridState

sealed interface ParkDetailScreenState {
    data object Loading : ParkDetailScreenState
    data class Loaded(
        val parkName: String,
        val description: String,
        val items: List<EncycGridState> = listOf()
    ) : ParkDetailScreenState

    data class Error(val message: String) : ParkDetailScreenState
}