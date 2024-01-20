package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.models.StateAudioPlayer

class AudioPlayerRepositoryImpl (private val mediaPlayer: MediaPlayer):
    AudioPlayerRepository {

    private var playerState = StateAudioPlayer.DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = StateAudioPlayer.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = StateAudioPlayer.PAUSED
    }

    override fun preparePlayer(url: String, statusBeenChanged: (s: StateAudioPlayer) -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnCompletionListener {
            playerState = StateAudioPlayer.PREPARED
            statusBeenChanged(StateAudioPlayer.PREPARED)
        }
        mediaPlayer.setOnPreparedListener {
            playerState = StateAudioPlayer.PREPARED
            statusBeenChanged(StateAudioPlayer.PREPARED)
        }
    }

    override fun changingPlayer(statusBeenChanged: (s: StateAudioPlayer) -> Unit) {
        when (playerState) {
            StateAudioPlayer.PLAYING -> {
                mediaPlayer.pause()
                playerState = StateAudioPlayer.PAUSED
                statusBeenChanged(playerState)
            }
            StateAudioPlayer.PREPARED, StateAudioPlayer.PAUSED -> {
                mediaPlayer.start()
                playerState = StateAudioPlayer.PLAYING
                statusBeenChanged(playerState)
            }
            StateAudioPlayer.DEFAULT -> {}
        }
    }

    override fun stoppingPlayer() {
        mediaPlayer.release()
    }
}