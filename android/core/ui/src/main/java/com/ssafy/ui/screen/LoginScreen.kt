package com.ssafy.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.ui.R

@Composable
fun LoginScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.logo),
                contentDescription = "app logo",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            //TODO 네이버 로그인 버튼 나중에 추가
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}