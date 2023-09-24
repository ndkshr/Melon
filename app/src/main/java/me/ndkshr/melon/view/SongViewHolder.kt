package me.ndkshr.melon.view

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Build
import android.util.Size
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import me.ndkshr.melon.R
import me.ndkshr.melon.databinding.CardSongDetailsBinding
import me.ndkshr.melon.model.AudioDetails
import java.lang.Exception

class SongViewHolder(
    private val binding: CardSongDetailsBinding,
    private val startSongCallback: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(audioDetails: AudioDetails) {
        binding.trackArtist.text = audioDetails.artist
        binding.trackName.text = audioDetails.title
        binding.trackArt.setImageResource(R.mipmap.ic_launcher_melon_round)
        binding.mainCard.setOnClickListener {
            startSongCallback.invoke(layoutPosition)
        }
    }
}