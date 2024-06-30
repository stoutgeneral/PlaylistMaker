package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlaylistTrackEntity

@Dao
interface PlaylistTrackDao {
    @Insert(entity = PlaylistTrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(playlistTrack: PlaylistTrackEntity)

    @Query("DELETE FROM playlist_track_entity WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun deleteTrack(playlistId: Int, trackId: Int)

    @Query ("DELETE FROM playlist_track_entity WHERE trackId = :trackId")
    suspend fun deleteAllTrack(trackId: Int)

    @Query("SELECT * FROM playlist_track_entity WHERE playlistId = :playlistId AND trackId = :trackId")
    suspend fun get(playlistId: Int, trackId: Int): PlaylistTrackEntity

    @Query("SELECT * FROM playlist_track_entity WHERE playlistId = :id ORDER BY id DESC")
    suspend fun getPlaylist(id: Int): List<PlaylistTrackEntity>?
}