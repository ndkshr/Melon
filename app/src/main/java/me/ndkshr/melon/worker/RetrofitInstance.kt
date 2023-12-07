package me.ndkshr.melon.worker

import android.content.Context
import com.google.gson.Gson
import me.ndkshr.melon.utils.PreferenceUtils
import me.ndkshr.melon.view.GOOEY_KEY_PREF
import me.ndkshr.melon.view.OPEN_AI_KEY_PREF
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

const val RUN_ID = "n9nva0x0"
const val UID = "3A0x8b1rfiao0eb9YhtlF0Ha4ml1"
const val GOOEY_BASE_URL = "https://api.gooey.ai/v2/"

val client: OkHttpClient = OkHttpClient.Builder()
    .connectTimeout(100, TimeUnit.SECONDS)
    .readTimeout(100, TimeUnit.SECONDS)
    .build()

val retrofitGooeyInstance: Retrofit = Retrofit.Builder()
    .baseUrl(GOOEY_BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val gooeyAPI: GooeyAPI = retrofitGooeyInstance.create(GooeyAPI::class.java)

fun getGooeyAuthKey(ctx: Context): String {
    return PreferenceUtils.getStringPref(ctx, GOOEY_KEY_PREF)
}

fun saveGooeyItems(ctx: Context, authKey: String) {
    PreferenceUtils.setStringPref(ctx, GOOEY_KEY_PREF, authKey)
}

const val OPEN_AI_BASE_URL = "https://api.openai.com/"

val retrofitOpenAIInstance: Retrofit = Retrofit.Builder()
    .baseUrl(OPEN_AI_BASE_URL)
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val openAIAPI: OpenAIAPI = retrofitOpenAIInstance.create(OpenAIAPI::class.java)

fun getOpenAIAuthKey(ctx: Context): String {
    return PreferenceUtils.getStringPref(ctx, OPEN_AI_KEY_PREF)
}

fun saveOpenAiItems(ctx: Context, authKey: String) {
    PreferenceUtils.setStringPref(ctx, OPEN_AI_KEY_PREF, authKey)
}
