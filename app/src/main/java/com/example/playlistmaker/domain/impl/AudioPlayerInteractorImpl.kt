package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.ui.audioplayer.StateAudioPlayer
import com.example.playlistmaker.domain.models.State

class AudioPlayerInteractorImpl (private val audioPlayerRepository: AudioPlayerRepository) :
    AudioPlayerRepository {
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

    override fun getCurrentState(): State {
        return State.DEFAULT
    }

    override fun getCurrentPosition(): Int = audioPlayerRepository.getCurrentPosition()
}