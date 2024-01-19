package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.util.Resource
import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTrack (expression: String): Resource<List<Track>>
}