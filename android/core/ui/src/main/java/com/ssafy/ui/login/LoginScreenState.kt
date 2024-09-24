package com.ssafy.ui.login

import androidx.compose.runtime.Immutable

sealed interface LoginScreenState {
    @Immutable
    data object Loading : LoginScreenState
    @Immutable
    data object Loaded : LoginScreenState
    @Immutable
    data class Error(val message: String) : LoginScreenState
}