package me.ndkshr.melon.worker

import com.google.gson.annotations.SerializedName

data class LyricOpenAiResponse(
    @SerializedName("choices")
    val choices: List<CompletionsChoices>
)

data class CompletionsChoices(
    val message: CompletionsMessage
)

data class CompletionsMessage(
    @SerializedName("role")
    val role: String,

    @SerializedName("content")
    val content: String
)
