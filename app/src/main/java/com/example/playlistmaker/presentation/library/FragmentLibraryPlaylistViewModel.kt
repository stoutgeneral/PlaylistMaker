package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.PlaylistInteractor
import com.example.playlistmaker.presentation.models.PlaylistState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FragmentLibraryPlaylistViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val playlistState = MutableLiveData<PlaylistState>(PlaylistState.Empty)
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState

    init {
        getPlaylist()
    }

    fun getPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getAll().collect {
                if (it.isEmpty()) renderState(PlaylistState.Empty)
                else renderState(PlaylistState.Content(it))
            }
        }
    }

    private fun renderState(state: PlaylistState) {
        playlistState.postValue(state)
    }
}