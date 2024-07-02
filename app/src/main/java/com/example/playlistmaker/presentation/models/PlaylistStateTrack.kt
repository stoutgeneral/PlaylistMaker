package com.example.playlistmaker.presentation.models

import com.example.playlistmaker.domain.models.Track

sealed interface PlaylistStateTrack {
    class Added(val name: String? = null): PlaylistStateTrack
    class Respond(val name: String? = null): PlaylistStateTrack
    class Content(val track: List<Track>): PlaylistStateTrack
    data object Empty: PlaylistStateTrack
}