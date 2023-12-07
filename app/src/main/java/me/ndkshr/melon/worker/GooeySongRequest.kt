package me.ndkshr.melon.worker

import com.google.gson.annotations.SerializedName

data class GooeySongRequest(
    @SerializedName("text_prompt")
    val testPrompt: String,

    @SerializedName("duration_sec")
    val duration: Int = 20
)