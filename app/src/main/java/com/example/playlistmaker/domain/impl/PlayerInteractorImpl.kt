package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.domain.PlayerInteractor

class PlayerInteractorImpl (private val playerInteractor: PlayerRepositoryImpl) : PlayerInteractor {
    override fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(url)
    }

    override fun startPlayer() {
        playerInteractor.startPlayer()
    }

    override fun pausePlayer() {
        playerInteractor.pausePlayer()
    }

    override fun playBackControl() {
        playerInteractor.playBackControl()
    }

    override fun statusIsOff() {
        playerInteractor.statusIsOff()
    }
}