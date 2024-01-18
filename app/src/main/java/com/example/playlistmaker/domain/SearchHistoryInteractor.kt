package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrackHistory (track: Track)
    fun getHistoryTrack (): ArrayList<Track>
    fun clearHistoryTrack ()
}