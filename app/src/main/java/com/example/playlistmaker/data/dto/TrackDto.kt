package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TrackDto (
        val trackId: Int,
        val trackName: String,
        val artistName: String,
        @SerializedName("trackTimeMillis") var trackTime: Long,
        val artworkUrl100: String,
        val collectionName: String,
        val releaseDate: String,
        val primaryGenreName: String,
        val country: String,
        val previewUrl: String
    ) : Serializable