package me.ndkshr.melon.worker

import com.google.gson.annotations.SerializedName
import java.io.StringBufferInputStream

data class GooeySongResponse(
    var title: String = "",
    val id: String,
    val url: String,

    @SerializedName("created_at")
    val createdAt: String,
    val output: GooeyOutput,
    var lyrics: String = ""
)

data class GooeyOutput(
    @SerializedName("output_audios")
    val outputAudios: OutputAudios
)

data class OutputAudios(
    @SerializedName("audio_ldm")
    val audioLDM: List<String>
)
