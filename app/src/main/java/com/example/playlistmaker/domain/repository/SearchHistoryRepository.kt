package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    // добавить метод по сохранению истории
    fun addTrackHistory (track: Track)
    fun getHistoryTrack (): ArrayList<Track>
    fun clearHistoryTrack ()
}