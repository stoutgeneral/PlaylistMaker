package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.ImageStorage
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.mapper.PlaylistMapper
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val storage: ImageStorage,
    private val playlistMapper: PlaylistMapper,
    private val appDataBase: AppDataBase
) : PlaylistRepository {
    override suspend fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = appDataBase.getPlaylistDao().getAll()
        playlists?.let { convertFromPlaylistEntity(it) }?.let { emit(it) }
    }

    override suspend fun add(playlist: Playlist) {
        if (playlist.uri != null && playlist.uri != "") {
            playlist.uri = storage.saveImageToPrivateStorage(playlist.uri!!)
        }
        appDataBase.getPlaylistDao().add(playlistMapper.map(playlist))
    }

    override suspend fun update(playlist: Playlist) {
        appDataBase.getPlaylistDao().update(playlistMapper.map(playlist))
    }

    private fun convertFromPlaylistEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlistMapper.map(it) }
    }

}