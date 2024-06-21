package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(entity = FavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun add (track: FavoriteEntity)

    @Delete(entity = FavoriteEntity::class)
    suspend fun delete(map: FavoriteEntity)

    @Query("SELECT * FROM favorite_entity ORDER BY insertTimeStamp DESC")
    suspend fun getAll(): List<FavoriteEntity>?

    @Query("SELECT trackId FROM favorite_entity")
    suspend fun getId(): List<Int>?
}