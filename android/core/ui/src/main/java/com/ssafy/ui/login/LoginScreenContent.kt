package com.ssafy.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.ui.R
import com.ssafy.ui.component.ErrorScreen
import com.ssafy.ui.component.LoadingScreen

@Composable
fun LoginScreenContent(
    modifier: Modifier = Modifier,
    loginScreenState: LoginScreenState,
    onIntent: (LoginUserIntent) -> Unit = {},
) {
    Scaffold(modifier = Modifier) { paddingValues ->
        when (loginScreenState) {
            is LoginScreenState.Loading -> {
                LoadingScreen(modifier = modifier.padding(paddingValues))
            }

            is LoginScreenState.Loaded -> {
                LoginScreenLoaded(
                    modifier = modifier.padding(paddingValues),
                    state = loginScreenState,
                    onIntent = onIntent,
                )
            }

            is LoginScreenState.Error -> {
                ErrorScreen(
                    modifier = modifier.padding(paddingValues),
                    message = loginScreenState.message,
                )
            }
        }
    }
}

@Composable
fun LoginScreenLoaded(
    modifier: Modifier,
    state: LoginScreenState.Loaded,
    onIntent: (LoginUserIntent) -> Unit,
) {
    Column(
        modifier = modifier.fillMaxSize().background(color = Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(id = R.drawable.logo),
            contentDescription = "app logo",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(48.dp))

        Image(
            modifier = Modifier.clickable { onIntent(LoginUserIntent.OnLogin) },
            painter = painterResource(id = R.drawable.naver_login),
            contentDescription = "네이버 로그인",
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreenContent(loginScreenState = LoginScreenState.Loaded, onIntent = {})
}
