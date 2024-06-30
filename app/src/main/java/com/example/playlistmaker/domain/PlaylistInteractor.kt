package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getAll(): Flow<List<Playlist>>
    suspend fun add(playlist: Playlist)
    suspend fun update(playlist: Playlist)
    suspend fun delete(playlistId: Int)
    suspend fun getId(id: Int): Flow<Playlist>
    suspend fun addPlaylistTrack(playlistTrack: PlaylistTrack)
    suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int)
    suspend fun getPlaylistTrack(playlistId: Int): Flow<List<Track>>
}