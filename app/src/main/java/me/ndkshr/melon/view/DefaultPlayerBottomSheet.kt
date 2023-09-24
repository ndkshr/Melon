package me.ndkshr.melon.view

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.ndkshr.melon.R
import me.ndkshr.melon.databinding.FragmentPlayerControlBinding
import me.ndkshr.melon.databinding.FragmentPlayerControlDefaultBinding
import me.ndkshr.melon.model.AudioDetails

private const val TAG = "DefaultPlayerBottomSheet"

class DefaultPlayerBottomSheet(
    private var audioDetails: AudioDetails
) : BottomSheetDialogFragment() {

    private lateinit var defaultPlayerBinding: FragmentPlayerControlDefaultBinding
    private lateinit var dialog: BottomSheetDialog
    private val exoPlayer by lazy { ExoPlayer.Builder(requireActivity()).build() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        defaultPlayerBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_player_control_default,
            container,
            false
        )
        return defaultPlayerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMediaAudio()
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
        exoPlayer.stop()
    }

    private fun setupMediaAudio() {
        defaultPlayerBinding.trackTitle.text = audioDetails.title
        defaultPlayerBinding.trackArtist.text = audioDetails.artist
        defaultPlayerBinding.styledPlayer.player = exoPlayer
        val mediaItem = MediaItem.fromUri(Uri.parse(audioDetails.path))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()

        exoPlayer.playWhenReady = true
    }

    companion object {
        fun show(audioDetails: AudioDetails, fm: FragmentManager?) {
            val fragment = DefaultPlayerBottomSheet(audioDetails)
            fragment.show(fm!!, TAG)
        }
    }
}