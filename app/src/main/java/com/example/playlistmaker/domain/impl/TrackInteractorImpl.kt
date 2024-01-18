package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource
import java.util.concurrent.Executors

class TrackInteractorImpl (private val trackRepository: TrackRepository) : TrackInteractor{

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(expression: String, consumer: (List<Track>?, Int?) -> Unit) {
        executor.execute {
            when (val resource = trackRepository.searchTrack(expression)) {
                is Resource.Success -> consumer(resource.data, null)
                is Resource.Error -> consumer(null, resource.message)
            }
        }
    }

    override fun consume(foundTrack: List<Track>?, errorMessage: Int?) {}
}