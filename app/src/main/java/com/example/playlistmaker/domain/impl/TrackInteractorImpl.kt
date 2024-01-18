package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.repository.TrackRepository
import com.example.playlistmaker.util.Resource

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = java.util.concurrent.Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTrack(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}