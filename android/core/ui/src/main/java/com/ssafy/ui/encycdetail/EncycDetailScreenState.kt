package com.ssafy.ui.encycdetail

sealed interface EncycDetailScreenState {
    data object Loading : EncycDetailScreenState
    data class Loaded(val data: EncycDetail) : EncycDetailScreenState
    data class Error(val message: String) : EncycDetailScreenState
}