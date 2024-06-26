package com.example.playlistmaker.ui.search

import com.example.playlistmaker.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState
    object Error : TracksState
    object Empty : TracksState
    object Default : TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

}