package com.ssafy.ui.login

sealed interface LoginScreenState {
    data object Loading : LoginScreenState
    data object Loaded : LoginScreenState
    data class Error(val message: String) : LoginScreenState
}