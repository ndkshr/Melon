package me.ndkshr.melon.model

import android.os.Parcelable
import android.provider.MediaStore
import kotlinx.android.parcel.Parcelize

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
}