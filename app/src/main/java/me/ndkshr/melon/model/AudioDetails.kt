package me.ndkshr.melon.model

import android.content.Context
import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize
import me.ndkshr.melon.utils.PreferenceUtils
import me.ndkshr.melon.worker.LYRIC_DELIM
import me.ndkshr.melon.worker.TITLE_DELIM

@Parcelize
data class AudioDetails(
    val id: Long? = 0L,
    val title: String? = "",
    val artist: String? = "",
    val albumID: String? = "",
    val duration: Long? = 0L,
    val path: String? = ""
) : Parcelable {

    companion object {
        const val TRACK_ID = MediaStore.Audio.Media._ID
        const val TRACK_TITLE = MediaStore.Audio.Media.TITLE
        const val TRACK_ARTIST = MediaStore.Audio.Media.ARTIST
        const val TRACK_ALBUM_ID = MediaStore.Audio.Media.ALBUM_ID
        const val TRACK_DURATION = MediaStore.Audio.Media.DURATION
        const val TRACK_DATA = MediaStore.Audio.Media.DATA
        fun getProjections() = arrayOf(
            TRACK_ID, TRACK_TITLE, TRACK_ARTIST, TRACK_ALBUM_ID, TRACK_DURATION, TRACK_DATA
        )
    }

    fun isGenerated(): Boolean = title?.contains("Generated") == true

    fun hasLyrics(): Boolean = title?.split(TITLE_DELIM)?.size!! >= 3 && title.split(TITLE_DELIM)[0] == "Generated"

    fun getAudioUUID() = title?.split(TITLE_DELIM)?.get(2)

    fun getLyrics(context: Context): List<String> = PreferenceUtils.getStringPref(context,
        getAudioUUID() ?: "").split(LYRIC_DELIM)
}