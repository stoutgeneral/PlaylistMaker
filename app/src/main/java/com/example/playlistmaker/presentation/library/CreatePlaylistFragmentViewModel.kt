package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist

open class CreatePlaylistFragmentViewModel (private val playlistInteractor: PlaylistInteractor): ViewModel() {

    var playlist = Playlist (tracks = ArrayList())
    private val playlistState = MutableLiveData<Playlist>()
    fun observePlaylistState(): LiveData<Playlist> = playlistState

    init {
        renderState(playlist)
    }

    private fun renderState(playlist: Playlist) {
        playlistState.postValue(playlist)
    }

    fun addName(name: String) {
        playlist.name = name
        renderState(playlist)
    }

    fun addImage(uri: String) {
        playlist.uri = uri
        renderState(playlist)
    }

    fun addDescription(description: String) {
        playlist.description = description
        renderState(playlist)
    }

    suspend fun playlistSave() {
        playlistInteractor.add(playlist)
    }
}