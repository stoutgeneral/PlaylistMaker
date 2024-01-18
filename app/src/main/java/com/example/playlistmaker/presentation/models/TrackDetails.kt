package com.example.playlistmaker.presentation.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class TrackDetails(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека в формате mm:ss
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseYear: String, // Год выпуска трека
    val primaryGenreName: String, // жанр
    val country: String, // Страна исполнителя
    val previewUrl: String, // отрывок трека
) : Serializable {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}