package com.ssafy.main.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.login.LoginScreenContent
import com.ssafy.ui.login.LoginUserIntent

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    LoginScreenContent(loginScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is LoginUserIntent.OnLogin -> onNavigateToHome()
            else -> viewModel.onIntent(intent)
        }
    })
}
