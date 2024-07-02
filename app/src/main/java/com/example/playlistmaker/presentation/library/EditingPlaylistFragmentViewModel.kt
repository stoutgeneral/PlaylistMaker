package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class EditingPlaylistFragmentViewModel (private val playlistInteractor: PlaylistInteractor) : CreatePlaylistFragmentViewModel(playlistInteractor) {

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    suspend fun savePlaylist (playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.update(playlist)
        }
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getId(id).collect {
                playlistLiveData.postValue(it)
                super.playlist = it
            }
        }
    }
}