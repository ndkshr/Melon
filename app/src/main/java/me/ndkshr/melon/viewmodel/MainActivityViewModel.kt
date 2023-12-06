package me.ndkshr.melon.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.ndkshr.melon.model.AudioDetails
import me.ndkshr.melon.worker.gooeyAPI
import me.ndkshr.melon.worker.GooeySongRequest
import me.ndkshr.melon.worker.GooeySongResponse
import me.ndkshr.melon.worker.LYRIC_DELIM
import me.ndkshr.melon.worker.OPEN_AI_AUTH_KEY
import me.ndkshr.melon.worker.openAIAPI
import java.net.SocketTimeoutException

const val TAG = "MainActivityViewModel"
class MainActivityViewModel(): ViewModel() {

    var songsList: List<AudioDetails> = mutableListOf()
    var currentSongPosition: MutableLiveData<Int> = MutableLiveData()

    val gooeyResponseLiveData: MutableLiveData<GooeySongResponse> = MutableLiveData()

    var currentGeneratedLyrics: MutableLiveData<String> = MutableLiveData("")

    fun getCurrentSong() = songsList.get(currentSongPosition.value ?: 0)

    fun isFirstSong() = currentSongPosition.value == 0
    fun isLastSong() = currentSongPosition.value == songsList.size - 1

    fun generateSongFromGooey(fileTitle: String, prompt: String, duration: Int = 20) =
        viewModelScope
        .launch {
        withContext(Dispatchers.IO) {
            try {
                val response = gooeyAPI.gooeyFetchSong(
                    GooeySongRequest(testPrompt = prompt, duration)
                )

                if (response.isSuccessful) {
                    val gooeyResponse = response.body()
                    if (gooeyResponse != null) {
                        gooeyResponse.title = fileTitle

//                    currentGeneratedLyrics = lyrics

                        gooeyResponseLiveData.postValue(gooeyResponse)
                    }
                    Log.d(TAG, "Gooey response success | WAV LINK = ${gooeyResponse?.url}")
                } else {
                    Log.d(TAG, "Song generation api failed")
                }
            } catch (e: SocketTimeoutException) {
                Log.d(TAG, "Socket Timeout!!!")
            } catch (e: Exception) {
                Log.d(TAG, "Unexpected error ==> $e")
            }
        }
    }

    fun generateLyricsFromOpenAi(prompt: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {

            try {
                val body = JsonObject()
                body.addProperty("model", "gpt-3.5-turbo")

                val messageArray = JsonArray()
                val message = JsonObject()
                message.addProperty("role", "user")
                message.addProperty(
                    "content",
                    "Create four short lines of song lyrics related to with no line numbers $prompt"
                )
                messageArray.add(message)
                body.add("messages", messageArray)

                val response = openAIAPI.fetchLyrics(
                    OPEN_AI_AUTH_KEY,
                    "application/json",
                    body
                )

                if (response.isSuccessful) {
                    var value = response.body()?.choices?.get(0)?.message?.content ?: ""
                    value = value.replace("\n", LYRIC_DELIM)
                    currentGeneratedLyrics.postValue(value)
                    Log.d(TAG, "Lyrics generation success")
                } else {
                    Log.d(TAG, "Lyrics generation failed")
                }
            }  catch (e: SocketTimeoutException) {
                Log.d(TAG, "Socket Timeout!!!")
            } catch (e: Exception) {
                Log.d(TAG, "Unexpected error ==> $e")
            }
        }
    }
}