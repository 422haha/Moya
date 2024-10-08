package com.ssafy.moya

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ssafy.moya.ui.theme.MoyaTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var ttsHelper: TTSHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MoyaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainNavigation(ttsHelper = ttsHelper)
                }
            }
        }
    }
}
