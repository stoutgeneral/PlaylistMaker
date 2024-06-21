package com.example.playlistmaker.data.repository


import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.db.entity.FavoriteEntity
import com.example.playlistmaker.data.mapper.FavoriteMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class FavoriteRepositoryImpl(private val appDataBase: AppDataBase, private val favoriteMapper: FavoriteMapper): FavoriteRepository {

    override suspend fun getAll(): Flow<List<Track>> = flow {
        val tracks = appDataBase.getFavoriteDao().getAll()
        tracks?.let { convertFavoritesEntity(it) }?.let { emit(it)}
    }

    override suspend fun add(track: Track) {
        appDataBase.getFavoriteDao().add(favoriteMapper.map(track))
    }

    override suspend fun delete(track: Track) {
        appDataBase.getFavoriteDao().delete(favoriteMapper.map(track))
    }

    private fun convertFavoritesEntity(tracks: List<FavoriteEntity>): List<Track> {
        return tracks.map {favoriteMapper.map(it)}
    }

}