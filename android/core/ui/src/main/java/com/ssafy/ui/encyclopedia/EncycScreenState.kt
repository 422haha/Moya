package com.ssafy.ui.encyclopedia

import androidx.compose.runtime.Immutable

sealed interface EncycScreenState {
    data object Loading : EncycScreenState
    data class Loaded(
        val selectedChipIndex: Int = 0,
        val items: List<EncycGridState> = listOf(),
        val progress: Float = 0f
    ) : EncycScreenState
    data class Error(val message: String) : EncycScreenState
}