package me.ndkshr.melon.worker

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaSession2
import android.media.MediaSession2Service
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.widget.ImageView
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.DefaultMediaDescriptionAdapter
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener
import com.google.android.exoplayer2.util.NotificationUtil.IMPORTANCE_HIGH
import me.ndkshr.melon.R
import me.ndkshr.melon.view.MainActivity

class PlayerForegroundService: Service() {
    enum class Actions {
        PLAY, PAUSE, STOP, NEXT, PREV
    }

    companion object {
        const val CHANNEL_ID = "melon_channel"
        const val NOTIFICATION_ID = 1003
    }

    private val mediaSession by lazy { MediaSession2.Builder(this).build() }
    private var exoPlayer:ExoPlayer? = null

    private var playerNotificationManager: PlayerNotificationManager? = null

    inner class ServiceBinder: Binder() {
        fun getPlayerService(): PlayerForegroundService {
            return this@PlayerForegroundService
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        val audioAttr: AudioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.AUDIO_CONTENT_TYPE_MUSIC)
            .build()

        exoPlayer = ExoPlayer.Builder(this).build()
        exoPlayer?.setAudioAttributes(audioAttr, true)

        playerNotificationManager = PlayerNotificationManager.Builder(this, NOTIFICATION_ID, CHANNEL_ID)
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(descriptionAdapter)
            .setChannelImportance(IMPORTANCE_HIGH)
            .setSmallIconResourceId(R.mipmap.ic_launcher_melon_round)
            .setChannelDescriptionResourceId(R.string.app_name)
            .setNextActionIconResourceId(R.drawable.ic_next)
            .setPreviousActionIconResourceId(R.drawable.ic_previous)
            .setPauseActionIconResourceId(R.drawable.ic_pause)
            .setPlayActionIconResourceId(R.drawable.ic_play)
            .setChannelNameResourceId(R.string.app_name)
            .build()

        playerNotificationManager!!.setPlayer(exoPlayer)
        playerNotificationManager!!.setPriority(NotificationCompat.PRIORITY_MAX)
        playerNotificationManager!!.setUseRewindAction(false)
        playerNotificationManager!!.setUseFastForwardAction(false)
    }

    val notificationListener = object : NotificationListener {
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopForeground(true)
            if (exoPlayer?.isPlaying == true) {
                exoPlayer?.pause()
            }
        }

        override fun onNotificationPosted(
            notificationId: Int,
            notification: Notification,
            ongoing: Boolean
        ) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            startForeground(notificationId, notification)
        }
    }

    val descriptionAdapter = object : MediaDescriptionAdapter {
        override fun getCurrentContentTitle(player: Player): CharSequence {
            return player.currentMediaItem?.mediaMetadata?.title ?: "Unknown Title"
        }

        override fun createCurrentContentIntent(player: Player): PendingIntent? {

            val openAppIntent = Intent(applicationContext, MainActivity::class.java)
            return PendingIntent.getActivity(applicationContext, 100, openAppIntent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        override fun getCurrentContentText(player: Player): CharSequence? {
            TODO("Not yet implemented")
        }

        override fun getCurrentLargeIcon(
            player: Player,
            callback: PlayerNotificationManager.BitmapCallback
        ): Bitmap? {
            val imageView = ImageView(applicationContext)
            imageView.setImageURI(exoPlayer?.currentMediaItem?.mediaMetadata?.artworkUri ?: Uri.EMPTY)

            var bd = imageView.drawable as BitmapDrawable?
            if (null == bd) {
                bd = ContextCompat.getDrawable(applicationContext, R.drawable.ic_launcher_foreground) as BitmapDrawable
            }

            return bd.bitmap
//            return null
        }

    }

    override fun onDestroy() {
        if (exoPlayer?.isPlaying == true) {
            exoPlayer?.stop()
        }

        playerNotificationManager?.setPlayer(null)
        exoPlayer?.release()
        exoPlayer = null

        stopForeground(true)
        stopSelf()

        super.onDestroy()
    }
}