package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.models.StateAudioPlayer

class AudioPlayerInteractorImpl (private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerRepository {
    override fun startPlayer() {
        audioPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        audioPlayerRepository.pausePlayer()
    }

    override fun preparePlayer(url: String, statusBeenChanged: (s: StateAudioPlayer) -> Unit) {
        audioPlayerRepository.preparePlayer(url, statusBeenChanged)
    }

    override fun changingPlayer(statusBeenChanged: (s: StateAudioPlayer) -> Unit) {
        audioPlayerRepository.changingPlayer(statusBeenChanged)
    }

    override fun stoppingPlayer() {
        audioPlayerRepository.stoppingPlayer()
    }
}