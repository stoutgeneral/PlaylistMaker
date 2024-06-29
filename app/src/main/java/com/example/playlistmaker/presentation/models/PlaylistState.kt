package com.example.playlistmaker.presentation.models

import com.example.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {
    data class Content (val playlists: List<Playlist>) : PlaylistState
    object Empty: PlaylistState
}