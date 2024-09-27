package com.ssafy.moya

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultLauncher
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class STTHelper
    @Inject
    constructor() {
        private lateinit var sttLauncher: ActivityResultLauncher<Intent>

        fun setLauncher(launcher: ActivityResultLauncher<Intent>) {
            this.sttLauncher = launcher
        }

        fun startSpeechToText() {
            val intent =
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "검색어를 말해주세요.")
                }
            sttLauncher.launch(intent)
        }

        fun handleActivityResult(
            resultCode: Int,
            data: Intent?,
            onResult: (String) -> Unit,
        ) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                onResult(result?.get(0).toString())
            }
        }
    }
