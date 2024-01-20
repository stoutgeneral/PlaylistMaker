package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.StateAudioPlayer


interface AudioPlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(url: String, statusBeenChanged: (s: StateAudioPlayer) -> Unit)
    fun changingPlayer (statusBeenChanged: (s: StateAudioPlayer) -> Unit)
    fun stoppingPlayer ()
}