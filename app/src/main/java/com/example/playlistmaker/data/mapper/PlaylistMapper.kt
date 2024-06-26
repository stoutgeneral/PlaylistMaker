package com.example.playlistmaker.data.mapper

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.models.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PlaylistMapper {

    private val gson = Gson()
    fun map(playlistEntity: PlaylistEntity): Playlist {
        var tracks: ArrayList<Int> = ArrayList()
        if (playlistEntity.tracks is String) {
            tracks = convertFromJson(playlistEntity.tracks)
        }
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            uri = playlistEntity.uri,
            description = playlistEntity.description,
            tracks = tracks,
            tracksCounter = playlistEntity.trackCounter
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
            return PlaylistEntity(
                id = playlist.id,
                name = playlist.name,
                uri = playlist.uri,
                description = playlist.description,
                tracks = convertToJson(playlist.tracks),
                trackCounter = playlist.tracksCounter
        )
    }

    private fun convertFromJson(json: String): ArrayList<Int> {
        val  item = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, item)
    }

    private fun convertToJson(tracks: ArrayList<Int>): String {
        return gson.toJson(tracks)
    }
}