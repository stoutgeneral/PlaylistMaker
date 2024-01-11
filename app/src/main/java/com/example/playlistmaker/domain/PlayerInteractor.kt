package com.example.playlistmaker.domain

interface PlayerInteractor {
    fun preparePlayer (url: String)
    fun startPlayer ()
    fun pausePlayer ()
    fun playBackControl ()
    fun statusIsOff ()
}