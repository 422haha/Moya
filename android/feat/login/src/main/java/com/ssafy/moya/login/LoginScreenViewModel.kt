package com.ssafy.moya.login

import androidx.lifecycle.ViewModel
import com.ssafy.network.ApiResponse
import com.ssafy.network.repository.UserRepository
import com.ssafy.ui.login.LoginScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoginScreenState>(LoginScreenState.Loading)
    val state: StateFlow<LoginScreenState> = _state

    fun load() {
        _state.value = LoginScreenState.Loaded
    }

    suspend fun login(provider: String, accessToken: String) : Boolean {
        val response = userRepository.login(provider, accessToken)
        return response is ApiResponse.Success
    }
}
