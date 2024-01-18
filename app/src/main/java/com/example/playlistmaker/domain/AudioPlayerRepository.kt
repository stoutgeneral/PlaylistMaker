package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.State

interface AudioPlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(url: String, statusBeenChanged: (s: State) -> Unit)
    fun changingPlayer (statusBeenChanged: (s: State) -> Unit)
    fun stoppingPlayer ()
}