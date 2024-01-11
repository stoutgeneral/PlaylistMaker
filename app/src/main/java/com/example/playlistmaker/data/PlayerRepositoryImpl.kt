package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.presentation.AudioPlayer

class PlayerRepositoryImpl : PlayerInteractor {

    // кажется понадобится метод для смены состояния, но это не точно
    private var playerState = AudioPlayer.State.DEFAULT
    private var player = MediaPlayer ()

    override fun preparePlayer(url: String) {
        player.apply {
            setDataSource(url)
            prepareAsync()
            setOnPreparedListener {
                playerState = AudioPlayer.State.PREPARED
            }
            setOnCompletionListener {
                playerState = AudioPlayer.State.PREPARED
            }
        }
    }

    override fun startPlayer() {
        player.start()
        playerState = AudioPlayer.State.PLAYING
    }

    override fun pausePlayer() {
        player.pause()
        playerState = AudioPlayer.State.PAUSED
    }

    override fun playBackControl() {
        when (playerState) {
            AudioPlayer.State.PLAYING -> pausePlayer()
            AudioPlayer.State.PREPARED, AudioPlayer.State.PAUSED -> startPlayer()
            else -> {}
        }
    }

    override fun statusIsOff() {
        player.release()
    }

}