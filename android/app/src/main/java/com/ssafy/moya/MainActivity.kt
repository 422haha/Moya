package com.ssafy.moya

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ssafy.moya.ui.theme.MoyaTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var ttsHelper: TTSHelper

    @Inject
    lateinit var sttHelper: STTHelper

    private lateinit var sttLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sttInit()

        setContent {
            MoyaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainNavigation(ttsHelper = ttsHelper)
                }
            }
        }
    }

    private fun sttInit() {
        sttLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                sttHelper.handleActivityResult(
                    resultCode = result.resultCode,
                    data = result.data,
                ) { recognizedText ->
                    Timber.tag("sttLauncher").d("인식된 Text: $recognizedText")
                }
            }
        sttHelper.setLauncher(sttLauncher)
    }
}
