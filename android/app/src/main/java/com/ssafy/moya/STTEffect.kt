package com.ssafy.moya

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import java.util.Locale

@Composable
fun STTEffect(
    onResult: (String?) -> Unit = {},
) {
    val sttLauncher =
        rememberLauncherForActivityResult(contract = SpeechRecognitionContract()) { result ->
            Log.d("TAG", "STTEffect: $result")
            onResult(result)
        }

    val intent =
        remember {
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM,
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PROMPT, "검색어를 말해주세요.")
            }
        }

    LaunchedEffect(Unit) {
        sttLauncher.launch(intent)
    }
}

class SpeechRecognitionContract : ActivityResultContract<Intent, String?>() {
    override fun createIntent(
        context: Context,
        input: Intent,
    ): Intent = input

    override fun parseResult(
        resultCode: Int,
        intent: Intent?,
    ): String? {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)
        }
        return null
    }
}
