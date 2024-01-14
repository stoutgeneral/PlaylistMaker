package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.AudioPlayerRepository
import com.example.playlistmaker.domain.models.State

class AudioPlayerUseCase : AudioPlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = State.DEFAULT

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = State.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = State.PAUSED
    }

    override fun preparePlayer(url: String, statusBeenChanged: (s: State) -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnCompletionListener {
            playerState = State.PREPARED
            statusBeenChanged(State.PREPARED)
        }
        mediaPlayer.setOnPreparedListener {
            playerState = State.PREPARED
            statusBeenChanged(State.PREPARED)
        }
    }

    override fun changingPlayer(statusBeenChanged: (s: State) -> Unit) {
        when (playerState) {
            State.PLAYING -> {
                mediaPlayer.pause()
                playerState = State.PAUSED
            }
            State.PREPARED, State.PAUSED -> {
                mediaPlayer.start()
                playerState = State.PLAYING
            }
            State.DEFAULT -> {}
        }
        statusBeenChanged(playerState)
    }

    override fun stoppingPlayer() {
        mediaPlayer.release()
    }
}