package com.ssafy.ui.encycdetail

import androidx.compose.runtime.Immutable

sealed interface EncycDetailScreenState {
    @Immutable
    data object Loading : EncycDetailScreenState
    @Immutable
    data class Loaded(val data: EncycDetail) : EncycDetailScreenState
    @Immutable
    data class Error(val message: String) : EncycDetailScreenState
}