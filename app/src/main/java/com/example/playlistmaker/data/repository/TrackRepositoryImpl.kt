package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl (private val networkClient: NetworkClient) : TrackRepository{
    override fun searchTrack (expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        when (response.resultCount) {
            200 -> {
                emit(Resource.Success((response as ITunesResponse).results.map { trackDto ->
                    TrackMapper.trackMap(trackDto = trackDto)
                }))
            }
            else -> {
                emit(Resource.Error(response.resultCount))
            }
        }
    }
}