package com.example.playlistmaker.presentation.library

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.FavoriteInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.FavoriteState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val favoriteInteractor: FavoriteInteractor): ViewModel() {

    private val favoriteState = MutableLiveData<FavoriteState>(FavoriteState.Empty)
    fun observeFavoriteState(): LiveData<FavoriteState> = favoriteState

    init {
        getTracks()
    }

    fun getTracks() {
        viewModelScope.launch {
            favoriteInteractor.getAll().collect {tracks -> processResults(tracks)
            }
        }
    }

    private fun processResults(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty)
        } else {
            for (i in tracks) {
                i.isFavorite = true
            }
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        favoriteState.postValue(state)
    }
}