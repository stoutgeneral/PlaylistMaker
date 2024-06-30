package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.domain.models.PlaylistTrack

class PlaylistTrackMapper {
    fun map(playlistTrack: PlaylistTrack): PlaylistTrackEntity {
        return PlaylistTrackEntity (
            id = playlistTrack.id,
            playlistId = playlistTrack.playlistId,
            trackId = playlistTrack.trackId,
            trackName = playlistTrack.trackName,
            artistName = playlistTrack.artistName,
            trackTimeMillis = playlistTrack.trackTimeMillis,
            artworkUrl100 = playlistTrack.artworkUrl100,
            collectionName = playlistTrack.collectionName,
            releaseDate = playlistTrack.releaseDate,
            primaryGenreName = playlistTrack.primaryGenreName,
            country = playlistTrack.country,
            previewUrl = playlistTrack.previewUrl
        )
    }

    fun map (playlistTrackEntity: PlaylistTrackEntity): PlaylistTrack {
        return PlaylistTrack (
            id = playlistTrackEntity.id,
            playlistId = playlistTrackEntity.playlistId,
            trackId = playlistTrackEntity.trackId,
            trackName = playlistTrackEntity.trackName,
            artistName = playlistTrackEntity.artistName,
            trackTimeMillis = playlistTrackEntity.trackTimeMillis,
            artworkUrl100 = playlistTrackEntity.artworkUrl100,
            collectionName = playlistTrackEntity.collectionName,
            releaseDate = playlistTrackEntity.releaseDate,
            primaryGenreName = playlistTrackEntity.primaryGenreName,
            country = playlistTrackEntity.country,
            previewUrl = playlistTrackEntity.previewUrl
        )
    }
}