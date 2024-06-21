package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.SearchHistoryInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun addTrackHistory(track: Track) {
        searchHistoryRepository.addTrackHistory(track = track)
    }

    override suspend fun getHistoryTrack(): ArrayList<Track> {
        return searchHistoryRepository.getFromHistory()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistoryTrack()
    }
}