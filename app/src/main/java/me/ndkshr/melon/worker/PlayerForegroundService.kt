package me.ndkshr.melon.worker

import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import me.ndkshr.melon.model.AudioDetails

class PlayerForegroundService: LifecycleService() {
    // TODO: Move the exoplayer here - declaration
    var player: ExoPlayer? = null
    var currentAudioDetails: AudioDetails? = null
    var playerNotificationManager: PlayerNotificationManager? = null
    var mediaSession: MediaSession? = null
    var mediaSessionConnector: MediaSessionConnector? = null

    override fun onCreate() {
        super.onCreate()
        // Create the player instance
        player = ExoPlayer.Builder(this).build()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.stop()
        player?.release()
    }

    companion object {
        private const val PLAYBACK_CHANNEL_ID = "playback_channel"
        private const val PLAYBACK_NOTIFICATION_ID = 1
        private const val ARG_AUDIO_DETAILS = "ARG_AUDIO_DETAILS"

        @MainThread
        fun newIntent(context: Context, audioDetails: AudioDetails) = Intent(
            context,
            PlayerForegroundService::class.java
        ).apply {
            putExtra(ARG_AUDIO_DETAILS, audioDetails)
        }
    }
}