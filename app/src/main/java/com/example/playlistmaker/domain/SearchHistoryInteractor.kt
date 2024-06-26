package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrackHistory (track: Track)
    suspend fun getHistoryTrack (): ArrayList<Track>
    fun clearHistory ()
}