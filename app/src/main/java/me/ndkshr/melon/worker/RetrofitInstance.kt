package me.ndkshr.melon.worker

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

const val RUN_ID = "n9nva0x0"
const val UID = "3A0x8b1rfiao0eb9YhtlF0Ha4ml1"
const val GOOEY_BASE_URL = "https://api.gooey.ai/v2/"
const val GOOEY_API_KEY = "sk-lFxQ082xK3tskMJclSbr8N6ZedtHHcUayn4FkpNqGaHwXThM"
const val GOOEY_BEARER_KEY = "Bearer $GOOEY_API_KEY"


val client: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(100, TimeUnit.SECONDS)
    .readTimeout(100, TimeUnit.SECONDS)
    .build()

val retrofitGooeyInstance: Retrofit = Retrofit.Builder()
    .baseUrl(GOOEY_BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val gooeyAPI = retrofitGooeyInstance.create(GooeyAPI::class.java)


const val OPEN_AI_BASE_URL = "https://api.openai.com/"
const val OPEN_AI_AUTH_KEY = "Bearer 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"

val retrofitOpenAIInstance: Retrofit = Retrofit.Builder()
    .baseUrl(OPEN_AI_BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val openAIAPI = retrofitOpenAIInstance.create(OpenAIAPI::class.java)