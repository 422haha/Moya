package com.ssafy.ui.parkdetail

import androidx.compose.runtime.Immutable
import com.ssafy.ui.encyclopedia.EncycGridState

sealed interface ParkDetailScreenState {
    @Immutable
    data object Loading : ParkDetailScreenState

    @Immutable
    data class Loaded(
        val parkName: String,
        val description: String,
        val parkImage: String? = null,
        val items: List<EncycGridState> = listOf()
    ) : ParkDetailScreenState

    @Immutable
    data class Error(val message: String) : ParkDetailScreenState
}