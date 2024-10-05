package com.ssafy.moya

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TTSHelper @Inject constructor(@ApplicationContext private val context: Context) : TextToSpeech.OnInitListener {
    private var tts: TextToSpeech = TextToSpeech(context, this)
    private var isInitialized = false

    private fun initTTS() {
        tts = TextToSpeech(context, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.KOREAN)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "언어 데이터가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
            } else {
                isInitialized = true
            }
        }
    }

    fun speak(text: String) {
        if (isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }

    fun shutdown() {
        tts.stop()
        tts.shutdown()
    }

    fun reStart() {
        shutdown()
        initTTS()
    }
}