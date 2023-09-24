package me.ndkshr.melon.view

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.ndkshr.melon.R
import me.ndkshr.melon.ViewModelFactory
import me.ndkshr.melon.databinding.FragmentPlayerControlBinding
import me.ndkshr.melon.model.AudioDetails
import me.ndkshr.melon.viewmodel.MainActivityViewModel

private const val TAG = "PlayerBottomSheetFragment"

class PlayerBottomSheetFragment() : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentPlayerControlBinding
    private lateinit var dialog: BottomSheetDialog
    private lateinit var audioDetails: AudioDetails
    private val exoPlayer by lazy { ExoPlayer.Builder(requireActivity()).build() }

    private val viewModelFactory = ViewModelFactory()
    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_player_control, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaAudio()

        viewModel.currentSongPosition.observe(viewLifecycleOwner) {
            setupMediaAudio()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        // show the mini player
    }

    private fun setupMediaAudio() {
        audioDetails = viewModel.getCurrentSong()

        binding.apply {
            artist.text = audioDetails.artist ?: "Unknown"
            title.text = audioDetails.title ?: "Unknown"
        }

        val mediaItem = MediaItem.fromUri(Uri.parse(audioDetails.path))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        binding.playOrPause.setOnClickListener {
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
                binding.playOrPause.setImageResource(R.drawable.ic_play)
            } else {
                exoPlayer.play()
                binding.playOrPause.setImageResource(R.drawable.ic_pause)
            }
            updatePlayerPositionProgress()
        }
        binding.playOrPause.setImageResource(R.drawable.ic_pause)
        binding.totalDuration.text = getReadableTime(audioDetails.duration?.toInt() ?: exoPlayer.duration.toInt())

        binding.next.setOnClickListener {
            if (viewModel.isLastSong()) {
                viewModel.currentSongPosition.postValue(0) // Play first song
            } else {
                viewModel.currentSongPosition.postValue(viewModel.currentSongPosition.value?.plus(1))
            }
        }

        // Play previous song
        binding.previous.setOnClickListener {
            if (exoPlayer.currentPosition < 2000) { // play previous if cur played for less than 2s
                if (viewModel.isFirstSong()) {
                    viewModel.currentSongPosition.postValue(
                        viewModel.songsList.size - 1 // play last song
                    )
                } else {
                    viewModel.currentSongPosition.postValue(
                        viewModel.currentSongPosition.value?.minus(1)
                    )
                }
            } else {
                exoPlayer.seekTo(0)
            }
        }

        exoPlayer.playWhenReady = true

        exoPlayer.addListener(object: Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)
                if (mediaItem == null) return

                resetPlayerProgress()
                updatePlayerPositionProgress()
            }

            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                if (playbackState == ExoPlayer.STATE_READY) {
                    resetPlayerProgress()
                    updatePlayerPositionProgress()
                } else if (playbackState == ExoPlayer.STATE_ENDED) {
                    binding.seekBar.progress = 0
                }
            }
        })
    }

    private fun resetPlayerProgress() {
        binding.seekBar.progress = exoPlayer.currentPosition.toInt()
        binding.seekBar.max = exoPlayer.duration.toInt()
    }

    private fun updatePlayerPositionProgress() {
        Handler().postDelayed({
            if (exoPlayer.isPlaying) {
                binding.seekBar.progress = exoPlayer.currentPosition.toInt()
                binding.timeGone.text = getReadableTime(exoPlayer.currentPosition.toInt())
            }

            updatePlayerPositionProgress()
        }, 1000)
    }

    private fun getReadableTime(totalDuration: Int): String {
        val time: String
        val hrs = totalDuration / (1000 * 60 * 60)
        val min = totalDuration % (1000 * 60 * 60) / (1000 * 60)
        val secs = totalDuration % (1000 * 60 * 60) % (1000 * 60 * 60) % (1000 * 60) / 1000

        var secString = "$secs"
        var minString = "$min"
        if (secs < 10) secString = "0$secs"
        if (min < 10) minString = "0$min"

        time = if (hrs < 1) {
            "$minString:$secString"
        } else {
            "$hrs:$minString:$secString"
        }
        return time
    }

    companion object {
        fun playCurrentSong(fm: FragmentManager?) {
            val fragment = PlayerBottomSheetFragment()
            fragment.show(fm!!, TAG)
        }
    }
}