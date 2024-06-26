package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDataBase: AppDataBase
) : TrackRepository {
    override fun searchTrack(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCount) {
            200 -> {
                val tracks = (response as ITunesResponse).results.map {
                    TrackMapper.trackMap(trackDto = it)
                }
                val favorites = appDataBase.getFavoriteDao().getId()
                if (favorites != null) {
                    setFavoritesToTrack(tracks, favorites)
                }
                emit(Resource.Success(tracks))
            }

            else -> {
                emit(Resource.Error(response.resultCount))
            }
        }
    }

    private fun setFavoritesToTrack (tracks: List<Track>, indicators: List<Int>) {
        for (i in tracks) {
            if (i.trackId in indicators) {
                i.isFavorite = true
            }
        }
    }
}