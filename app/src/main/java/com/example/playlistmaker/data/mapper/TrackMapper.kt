package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

object TrackMapper {
    fun trackMap (trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTimeMillis = trackDto.trackTimeMillis,
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
    }

    fun map(trackPlaylist: PlaylistTrackEntity): Track {
        return Track(
            trackId = trackPlaylist.trackId,
            trackName = trackPlaylist.trackName,
            artistName = trackPlaylist.artistName,
            trackTimeMillis = trackPlaylist.trackTimeMillis,
            artworkUrl100 = trackPlaylist.artworkUrl100,
            collectionName = trackPlaylist.collectionName,
            releaseDate = trackPlaylist.releaseDate,
            primaryGenreName = trackPlaylist.primaryGenreName,
            country = trackPlaylist.country,
            previewUrl = trackPlaylist.previewUrl
        )
    }
}