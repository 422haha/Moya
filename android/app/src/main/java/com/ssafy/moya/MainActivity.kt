package com.ssafy.moya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ssafy.moya.ui.theme.MoyaTheme
import com.ssafy.ui.screen.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var ttsHelper: TTSHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ttsHelper = TTSHelper(this)
        setContent {
            MoyaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainNavigation(ttsHelper = ttsHelper)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    MoyaTheme {
        HomeScreen()
    }
}