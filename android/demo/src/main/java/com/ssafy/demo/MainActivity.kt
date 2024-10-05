package com.ssafy.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.ssafy.ar.ui.ARSceneComposable
import com.ssafy.demo.ui.theme.MoyaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        enableEdgeToEdge()

        setContent {
            MoyaTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    ARSceneComposable(
                        4,
                        onTTSClicked = {},
                        onTTSReStart = {},
                        onTTSShutDown = {},
                        onPop = {},
                    )
                }
            }
        }
    }
}