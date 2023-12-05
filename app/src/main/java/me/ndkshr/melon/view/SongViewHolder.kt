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
import me.ndkshr.melon.worker.TITLE_DELIM
import java.lang.Exception

class SongViewHolder(
    private val binding: CardSongDetailsBinding,
    private val startSongCallback: (Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(audioDetails: AudioDetails) {
        if (audioDetails.isGenerated()) {
            binding.trackArtist.text = "Melon AI"
            binding.trackName.text = if (audioDetails.hasLyrics()) {
                audioDetails.title!!.split(TITLE_DELIM)[1]
            } else {
                audioDetails.title
            }
            binding.trackArtist.setTextColor(binding.root.context.getColor(R.color.white))
            binding.trackName.setTextColor(binding.root.context.getColor(R.color.mint))
            binding.trackArt.setImageResource(R.mipmap.ic_green_melon_new_round)
        } else {
            binding.trackArtist.text = audioDetails.artist
            binding.trackName.text = audioDetails.title
            binding.trackArt.setImageResource(R.mipmap.ic_launcher_melon_round)
        }

        binding.mainCard.setOnClickListener {
            startSongCallback.invoke(layoutPosition)
        }
    }
}