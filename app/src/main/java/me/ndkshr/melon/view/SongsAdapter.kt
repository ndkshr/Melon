package me.ndkshr.melon.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.ndkshr.melon.databinding.CardSongDetailsBinding
import me.ndkshr.melon.model.AudioDetails
import me.ndkshr.melon.viewmodel.MainActivityViewModel

class SongsAdapter(
    private val viewModel: MainActivityViewModel,
    private val startAudioCallback: (Int) -> Unit
): RecyclerView.Adapter<SongViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        return SongViewHolder(
            CardSongDetailsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            startAudioCallback
        )
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(viewModel.songsList[position])
    }

    override fun getItemCount(): Int {
        return viewModel.songsList.size
    }

}