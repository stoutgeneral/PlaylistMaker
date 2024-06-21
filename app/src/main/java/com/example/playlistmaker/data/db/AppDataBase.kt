package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.FavoriteDao
import com.example.playlistmaker.data.db.entity.FavoriteEntity

@Database(
    version = 1,
    entities = [FavoriteEntity::class],
    exportSchema = false
)
abstract class AppDataBase: RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
}