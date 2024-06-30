package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.PlaylistInteractor
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl (private val repository: PlaylistRepository) : PlaylistInteractor {
    override suspend fun getAll(): Flow<List<Playlist>> {
        return repository.getAll()
    }

    override suspend fun add(playlist: Playlist) {
        repository.add(playlist)
    }

    override suspend fun update(playlist: Playlist) {
        repository.update(playlist)
    }

    override suspend fun delete(playlistId: Int) {
        repository.delete(playlistId)
    }

    override suspend fun getId(id: Int): Flow<Playlist> {
        return repository.getId(id)
    }

    override suspend fun addPlaylistTrack(playlistTrack: PlaylistTrack) {
        return repository.addPlaylistTrack(playlistTrack)
    }

    override suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        return repository.deletePlaylistTrack(playlistId, trackId)
    }

    override suspend fun getPlaylistTrack(playlistId: Int): Flow<List<Track>> {
        return repository.getPlaylistTrack(playlistId)
    }

}