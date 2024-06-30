package com.example.playlistmaker.data.mapper
import com.example.playlistmaker.data.db.entity.FavoriteEntity
import com.example.playlistmaker.domain.models.Track

class FavoriteMapper {
    fun map (trackFv: FavoriteEntity): Track {
        return Track(
            trackId = trackFv.trackId,
            trackName = trackFv.trackName,
            artistName = trackFv.artistName,
            trackTimeMillis = trackFv.trackTimeMillis,
            artworkUrl100 = trackFv.artworkUrl100,
            collectionName = trackFv.collectionName,
            releaseDate = trackFv.releaseDate,
            primaryGenreName = trackFv.primaryGenreName,
            country = trackFv.country,
            previewUrl = trackFv.previewUrl
        )
    }

    fun map(track: Track): FavoriteEntity {
        return FavoriteEntity(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
}