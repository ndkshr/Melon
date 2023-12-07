package me.ndkshr.melon.worker

import android.app.PendingIntent
import android.graphics.Bitmap
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter

class DescriptionAdapter: MediaDescriptionAdapter {
    override fun getCurrentContentTitle(player: Player): CharSequence {
        return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
    }

    override fun createCurrentContentIntent(player: Player): PendingIntent? {

//        val openAppIntent = Intent()
        return null;
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        TODO("Not yet implemented")
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        TODO("Not yet implemented")
    }
}