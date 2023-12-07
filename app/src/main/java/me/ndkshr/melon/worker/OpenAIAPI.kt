package me.ndkshr.melon.worker

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIAPI {

    @POST("v1/chat/completions")
    suspend fun fetchLyrics(
        @Header("Authorization") authKey: String,
        @Header("Content-Type") contentType: String,
        @Body body: JsonObject
    ): Response<LyricOpenAiResponse>
}