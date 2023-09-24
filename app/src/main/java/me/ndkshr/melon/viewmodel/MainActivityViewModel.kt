package me.ndkshr.melon.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.ndkshr.melon.model.AudioDetails

class MainActivityViewModel(): ViewModel() {

    var songsList: List<AudioDetails> = mutableListOf()
    var currentSongPosition: MutableLiveData<Int> = MutableLiveData()

    fun getCurrentSong() = songsList.get(currentSongPosition.value ?: 0)

    fun isFirstSong() = currentSongPosition.value == 0
    fun isLastSong() = currentSongPosition.value == songsList.size - 1
}