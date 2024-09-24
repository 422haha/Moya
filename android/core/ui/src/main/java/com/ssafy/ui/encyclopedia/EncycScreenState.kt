package com.ssafy.ui.encyclopedia

import androidx.compose.runtime.Immutable

sealed interface EncycScreenState {
    @Immutable
    data object Loading : EncycScreenState
    @Immutable
    data class Loaded(
        val selectedChipIndex: Int = 0,
        val items: List<EncycGridState> = listOf(),
        val progress: Float = 0f
    ) : EncycScreenState
    @Immutable
    data class Error(val message: String) : EncycScreenState
}