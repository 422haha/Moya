package com.ssafy.main.login

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ssafy.ui.login.LoginScreenContent
import com.ssafy.ui.login.LoginUserIntent

@Composable
fun LoginScreen(
    viewModel: LoginScreenViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val upload = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { result ->
        result?.let {
            viewModel.upload(context, it)  
        } 
    }
    
    LaunchedEffect(key1 = Unit) {
        upload.launch("image/*")
    }

    val uiState by viewModel.state.collectAsStateWithLifecycle()
    LoginScreenContent(loginScreenState = uiState, onIntent = { intent ->
        when (intent) {
            is LoginUserIntent.OnLogin -> onNavigateToHome()
            else -> viewModel.onIntent(intent)
        }
    })
}