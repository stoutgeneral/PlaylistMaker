package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.PlaylistInteractor
import com.example.playlistmaker.domain.ShareInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.presentation.models.PlaylistStateTrack
import kotlinx.coroutines.launch

class DetailsPlaylistFragmentViewModel (private val playlistInteractor: PlaylistInteractor, private val shareInteractor: ShareInteractor) : ViewModel() {

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistLiveData

    private val trackLiveData = MutableLiveData<PlaylistStateTrack>()
    fun observeTrack(): LiveData<PlaylistStateTrack> = trackLiveData

    fun shareAppPlaylist(playlist: String) {
        shareInteractor.sharePlaylist(playlist)
    }

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getId(id).collect {
                playlistLiveData.postValue(it)
            }
        }
    }

    fun getPlaylistTrack(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistTrack(id).collect {
                if (it.isEmpty()) {
                    renderState(PlaylistStateTrack.Empty)
                } else {
                    renderState(PlaylistStateTrack.Content(it))
                }
            }
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.delete(playlistId)
        }
    }

    fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistTrack(playlistId, trackId)
            getPlaylist(playlistId)
            getPlaylistTrack(playlistId)
        }
    }

    private fun renderState (state: PlaylistStateTrack) {
        trackLiveData.postValue(state)
    }
}