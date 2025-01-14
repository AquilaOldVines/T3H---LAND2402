package core

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import data.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainActivityViewModel(val repository: MusicRepository) : ViewModel() {

    private val _allSongs = MutableStateFlow<List<Song>>(emptyList())
    public val allSongs = _allSongs.asStateFlow()

    init {

        viewModelScope.launch {

            _allSongs.value = repository.getAllSongs()
        }
    }

    class Factory(val context: Context) : ViewModelProvider.Factory {

        override fun <T : ViewModel> create (modelClass: Class<T>): T {

            return MainActivityViewModel(
                MusicRepository(context.applicationContext)
            ) as T
        }
    }
}