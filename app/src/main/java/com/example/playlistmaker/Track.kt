package com.example.playlistmaker

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") var trackTime: Long,
    val artworkUrl100: String,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String
) : Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
    fun getSimpleDateFormat(variableChange: Long) = SimpleDateFormat("mm:ss", Locale.getDefault()).format(variableChange)
}


