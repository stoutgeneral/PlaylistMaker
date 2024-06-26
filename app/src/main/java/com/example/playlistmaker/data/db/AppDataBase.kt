package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteDao
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.entity.FavoriteEntity
import com.example.playlistmaker.data.db.entity.PlaylistEntity

@Database(
    version = 2,
    entities = [FavoriteEntity::class, PlaylistEntity::class],
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun getPlaylistDao(): PlaylistDao
}