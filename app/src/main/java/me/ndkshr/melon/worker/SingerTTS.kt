package me.ndkshr.melon.worker

import android.content.Context
import android.speech.tts.TextToSpeech
import com.google.android.exoplayer2.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class SingerTTS(private val context: Context) {

    private var ttsInstance: TextToSpeech? = null

    private val job: Job = SupervisorJob()

    private val ttsCoroutineScope = CoroutineScope(job + Dispatchers.Main.immediate)

    init {
        ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result: Int = ttsInstance!!.setLanguage(Locale("es_ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Log.e("TTS", "Language not supported")
                }
            } else {
                Log.e("TTS", "Initialization failed")
            }
        }
    }

    fun sing(lyrics: List<String>) = ttsCoroutineScope.launch {
        withContext(Dispatchers.Default) {
            lyrics.forEach { line ->
                ttsInstance?.setSpeechRate(0.8f)
                ttsInstance?.speak(line, TextToSpeech.QUEUE_FLUSH, null)
                delay(4000L)
            }
        }
    }

    fun destroy() {
        ttsInstance?.stop()
        ttsInstance?.shutdown()
    }
}