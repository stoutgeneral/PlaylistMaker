package com.example.playlistmaker.data.repository

import android.media.MediaPlayer
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.ui.audioplayer.StateAudioPlayer

class AudioPlayerRepositoryImpl (private val mediaPlayer: MediaPlayer):
    AudioPlayerRepository {

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
                statusBeenChanged(playerState)
            }
            State.PREPARED, State.PAUSED -> {
                mediaPlayer.start()
                playerState = State.PLAYING
                statusBeenChanged(playerState)
            }
            State.DEFAULT -> {}
        }
    }

    override fun stoppingPlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentState(): State {
        TODO("Not yet implemented")
    }

    override fun getCurrentPosition(): Int {
        TODO("Not yet implemented")
    }
}