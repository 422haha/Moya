package com.ssafy.main.login

import androidx.lifecycle.ViewModel
import com.ssafy.ui.login.LoginScreenState
import com.ssafy.ui.login.LoginUserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Loading)
    val state: StateFlow<LoginScreenState> = _state

    fun onIntent(intent: LoginUserIntent) {

    }
}