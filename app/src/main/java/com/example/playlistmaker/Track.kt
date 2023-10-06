package com.example.playlistmaker

data class Track(
    val trackName: String,
    val artistName: String,
    var trackTimeMillis: Long,
    val artworkUrl100: String
)
