package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.ITunesResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.TrackMap
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource

class TrackRepositoryImpl (private val networkClient: NetworkClient) : TrackRepository{
    override fun searchTrack (expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))

        return when (response.resultCount) {
            200 -> {
                Resource.Success((response as ITunesResponse).results.map { trackDto ->
                    TrackMap.trackMap(trackDto = trackDto)
                })
            }
            else -> {
                Resource.Error(response.resultCount)
            }
        }
    }
}