package me.ndkshr.melon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import me.ndkshr.melon.viewmodel.MainActivityViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainActivityViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
