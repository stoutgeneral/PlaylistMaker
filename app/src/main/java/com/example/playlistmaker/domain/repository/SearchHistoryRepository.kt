package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun addTrackHistory(track: Track)
    fun getFromHistory(): ArrayList<Track>
    fun clearHistoryTrack()
}