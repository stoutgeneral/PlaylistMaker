package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface TrackInteractor {
    fun searchTrack(expression: String, consumer: (List<Track>?, Int?) -> Unit)
    fun consume(foundTrack: List<Track>?, errorMessage: Int?)
}