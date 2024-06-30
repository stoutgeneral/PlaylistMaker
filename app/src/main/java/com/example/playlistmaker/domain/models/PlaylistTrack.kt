package com.example.playlistmaker.domain.models

data class PlaylistTrack(
    val id: Int?,
    val playlistId: Int,
    val trackId: Int,
    val trackName: String? = null,
    val artistName: String? = null,
    val trackTimeMillis: Int? = 0,
    val artworkUrl100: String? = null,
    val collectionName: String? = null,
    val releaseDate: String? = null,
    val primaryGenreName: String? = null,
    val country: String? = null,
    val previewUrl: String? = null
)
