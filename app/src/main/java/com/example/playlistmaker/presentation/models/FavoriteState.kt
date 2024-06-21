package com.example.playlistmaker.presentation.models

import com.example.playlistmaker.domain.models.Track

sealed interface FavoriteState {
    data class Content(val tracks: List<Track>) : FavoriteState

    data object Empty: FavoriteState
}