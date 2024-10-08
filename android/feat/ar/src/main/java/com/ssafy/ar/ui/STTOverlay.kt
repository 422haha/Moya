package com.ssafy.ar.ui

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Locale

@Composable
fun STTOverlay(
    onDismiss: () -> Unit,
    onResult: (String?) -> Unit,
) {
    val context = LocalContext.current
    var recognizedText by remember { mutableStateOf("") }
    var isListening by remember { mutableStateOf(false) }
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer.destroy()
        }
    }

    val recognitionListener =
        object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    recognizedText = matches[0]
                    onResult(recognizedText)
                }
                isListening = false
            }

            override fun onReadyForSpeech(params: Bundle?) {
                isListening = true
            }

            override fun onEndOfSpeech() {
                isListening = false
            }

            override fun onError(error: Int) {
                isListening = false
                onResult(null)
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onPartialResults(partialResults: Bundle?) {}

            override fun onEvent(
                eventType: Int,
                params: Bundle?,
            ) {}
        }

    LaunchedEffect(speechRecognizer) {
        speechRecognizer.setRecognitionListener(recognitionListener)
        startSpeechToText(speechRecognizer)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier =
                Modifier
                    .wrapContentSize(),
            color = Color.Transparent,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = if (isListening) "궁금한 것을 물어보세요!" else "준비 중...",
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(24.dp))
                CircularProgressIndicator(color = Color.White)
            }
        }
    }
}

private fun startSpeechToText(speechRecognizer: SpeechRecognizer) {
    val intent =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
    speechRecognizer.startListening(intent)
}
