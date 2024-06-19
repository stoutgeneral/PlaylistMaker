package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int?>>
}