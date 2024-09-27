package com.ssafy.ui.encyclopedia

import androidx.compose.runtime.Immutable
import com.ssafy.ui.component.EncycCardState

sealed interface EncycScreenState {
    @Immutable
    data object Loading : EncycScreenState

    @Immutable
    data class Loaded(
        val selectedChipIndex: Int = 0,
        val items: List<EncycCardState> = listOf(),
        val progress: Float = 0f,
    ) : EncycScreenState

    @Immutable
    data class Error(
        val message: String,
    ) : EncycScreenState
}
