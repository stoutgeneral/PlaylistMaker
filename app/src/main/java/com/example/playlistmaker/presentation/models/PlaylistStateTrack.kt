package com.example.playlistmaker.presentation.models

sealed interface PlaylistStateTrack {
    class Added(val name: String? = null): PlaylistStateTrack
    class Respond(val name: String? = null): PlaylistStateTrack
}