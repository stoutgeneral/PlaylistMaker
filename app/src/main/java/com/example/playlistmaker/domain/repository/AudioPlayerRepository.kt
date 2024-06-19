package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.ui.audioplayer.StateAudioPlayer


interface AudioPlayerRepository {
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer(url: String, statusBeenChanged: (s: State) -> Unit)
    fun changingPlayer(statusBeenChanged: (s: State) -> Unit)
    fun stoppingPlayer()
    fun getCurrentState(): State
    fun getCurrentPosition(): Int
}