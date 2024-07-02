package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.ImageStorage
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.data.mapper.PlaylistMapper
import com.example.playlistmaker.data.mapper.PlaylistTrackMapper
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val storage: ImageStorage,
    private val playlistMapper: PlaylistMapper,
    private val trackMapper: TrackMapper,
    private val playlistTrackMapper: PlaylistTrackMapper,
    private val appDataBase: AppDataBase
) : PlaylistRepository {
    override suspend fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = appDataBase.getPlaylistDao().getAll()
        playlists?.let { convertPlaylistEntity(it) }?.let { emit(it) }
    }

    override suspend fun add(playlist: Playlist) {
        playlist.uri = saveImage(playlist.uri)
        appDataBase.getPlaylistDao().add(playlistMapper.map(playlist))
    }

    override suspend fun update(playlist: Playlist) {
        playlist.uri = saveImage(playlist.uri)
        appDataBase.getPlaylistDao().update(playlistMapper.map(playlist))
    }

    override suspend fun delete(playlistId: Int) {
        appDataBase.getPlaylistDao().delete(playlistId)
    }

    override suspend fun addPlaylistTrack(playlistTrack: PlaylistTrack) {
        appDataBase.getPlaylistTrackDao().addTrack(playlistTrackMapper.map(playlistTrack))
    }

    override suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        val playlistTrackEntity: PlaylistTrackEntity = appDataBase.getPlaylistTrackDao().get(playlistId, trackId)
        var playlistEntity: PlaylistEntity = appDataBase.getPlaylistDao().getId(playlistId)
        val playlist: Playlist = playlistMapper.map(playlistEntity)

        if (playlist.tracks.contains(trackId)) {
            playlist.tracks.remove(trackId)
            playlist.trackTimeMillis = playlist.trackTimeMillis - playlistTrackEntity.trackTimeMillis!!
        }
        playlistEntity = playlistMapper.map(playlist)

        appDataBase.getPlaylistDao().update(playlistEntity)
        appDataBase.getPlaylistTrackDao().deleteTrack(playlistId, trackId)

        if (checkUnusedTracks(trackId)) appDataBase.getPlaylistTrackDao().deleteAllTrack(trackId)
    }

    override suspend fun getPlaylistTrack(playlistId: Int): Flow<List<Track>> = flow {
        val track = appDataBase.getPlaylistTrackDao().getPlaylist(playlistId)?.map {
            trackMapper.map(it)
        }
        val favorites = appDataBase.getFavoriteDao().getId()
        if (favorites != null && track != null) setFavoritesTracks(track, favorites)
        if (track != null) emit(track)
    }

    override suspend fun getId(id: Int): Flow<Playlist> = flow {
        val item = appDataBase.getPlaylistDao().getId(id)
        emit(playlistMapper.map(item))
    }

    private fun convertPlaylistEntity(playlist: List<PlaylistEntity>): List<Playlist> {
        return playlist.map { playlistMapper.map(it) }
    }

    private fun setFavoritesTracks(tracks: List<Track>, indicators: List<Int>) {
        for (i in tracks) {
            if (i.trackId in indicators) {
                i.isFavorite = true
            }
        }
    }

    private suspend fun checkUnusedTracks(trackId: Int): Boolean {
        val playlist = appDataBase.getPlaylistDao().getAll()?.let { convertPlaylistEntity(it) }

        return if (playlist?.isEmpty() == true) {
            true
        } else {
            val playlistWithTrack = playlist!!.filter { playlist ->
                playlist.tracks.contains(trackId)
            }
            playlistWithTrack.isEmpty()
        }
    }

    private fun saveImage(image: String?): String {
        return if (image?.isEmpty() == false) storage.saveImageToPrivateStorage(image) else ""
    }

}