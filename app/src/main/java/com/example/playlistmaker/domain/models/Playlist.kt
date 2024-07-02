package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Int? = null,
    var name: String? = null,
    var uri: String? = null,
    var description: String? = null,
    val tracks: ArrayList<Int>,
    var trackTimeMillis: Int = 0
)