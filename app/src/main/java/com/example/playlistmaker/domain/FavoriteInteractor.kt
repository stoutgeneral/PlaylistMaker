package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun getAll(): Flow<List<Track>>
    suspend fun add(track: Track)
    suspend fun delete(track: Track)
}