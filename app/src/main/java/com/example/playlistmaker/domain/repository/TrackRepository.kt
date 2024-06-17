package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackRepository {
    fun searchTrack (expression: String): Flow<Resource<List<Track>>>
}