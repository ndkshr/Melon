package me.ndkshr.melon.worker

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.provider.MediaStore.Audio.Media
import me.ndkshr.melon.model.AudioDetails

class FetchSongsWorker(
    private val context: Context
) {
    fun fetchSongs(): List<AudioDetails> {
        val cursor = getCursor()
        val audioList = mutableListOf<AudioDetails>()
        cursor?.let {
            while (it.moveToNext()) {
                val id = it.getLong(it.getColumnIndex(AudioDetails.TRACK_ID))
                val title = it.getString(it.getColumnIndex(AudioDetails.TRACK_TITLE))
                val artist = it.getString(it.getColumnIndex(AudioDetails.TRACK_ARTIST))
                val albumId = it.getString(it.getColumnIndex(AudioDetails.TRACK_ALBUM_ID))
                val duration = it.getLong(it.getColumnIndex(AudioDetails.TRACK_DURATION))
                val path = it.getString(it.getColumnIndex(AudioDetails.TRACK_DATA))
                val audio = AudioDetails(
                    title = title, artist = artist, albumID = albumId,
                    id = id, duration = duration, path = path
                )
                audioList.add(audio)
            }
        }

        audioList.forEach { audio ->
            val albumCursor = getAlbumCursor(audio.albumID ?: "")
            albumCursor?.let { c ->
                if (c.moveToNext()) {
                    audio.albumID?.let { id ->
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL),
                            id.toLong()
                        )
                    }
//                    val albumArt = c.getString(c.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART))
                }
            }
        }
        cursor?.close()
        return audioList
    }

    private fun getCursor(): Cursor? {
        val contentResolver = context.contentResolver
        val uri = Media.EXTERNAL_CONTENT_URI
        val projections = AudioDetails.getProjections()
        return contentResolver
            .query(uri, projections, null, null, null)
    }

    private fun getAlbumCursor(albumId: String): Cursor? {
        val contentResolver = context.contentResolver
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        return contentResolver.query(
            uri,
            null,
            MediaStore.Audio.Albums._ID + "=?",
            arrayOf<String>(java.lang.String.valueOf(albumId)),
            null
        )
    }
}