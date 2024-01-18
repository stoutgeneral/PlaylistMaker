package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.AudioPlayerInteractor
import com.example.playlistmaker.domain.AudioPlayerRepository
import com.example.playlistmaker.domain.models.State

class AudioPlayerUseCaseImpl (private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerInteractor {
    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun preparePlayer(url: String, statusBeenChanged: (s: State) -> Unit) {
        audioPlayerRepository.preparePlayer(url, statusBeenChanged)
    }

    override fun changingPlayer(statusBeenChanged: (s: State) -> Unit) {
        audioPlayerRepository.changingPlayer(statusBeenChanged)
    }

    override fun stoppingPlayer() {
        audioPlayerRepository.stoppingPlayer()
    }
}