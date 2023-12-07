package me.ndkshr.melon.worker

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri


const val TITLE_DELIM = "+"
const val LYRIC_DELIM = "#"
class GooeySongDownloadManager(
    private val context: Context
):  SongDownloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)
    override fun downloadFile(title: String, url: String): Long {
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("audio/wav")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(title)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.wav")

        return downloadManager.enqueue(request)
    }
}