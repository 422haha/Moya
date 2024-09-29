package com.ssafy.moya.login

import androidx.lifecycle.ViewModel
import com.ssafy.ui.login.LoginScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginScreenViewModel : ViewModel() {
    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Loading)
    val state: StateFlow<LoginScreenState> = _state

    fun load() {
        _state.value = LoginScreenState.Loaded
    }
}
