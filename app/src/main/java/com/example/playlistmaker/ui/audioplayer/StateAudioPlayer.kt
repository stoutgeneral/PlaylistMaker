package com.example.playlistmaker.ui.audioplayer

import com.example.playlistmaker.R

sealed class StateAudioPlayer(val checkingButtonStatus: Boolean, val buttonState: Int, val progress: String) {
        abstract fun isPrepared(): Boolean

        class Default : StateAudioPlayer(false, R.drawable.pause_track, "00:00") {
                override fun isPrepared(): Boolean = false
        }

        class Prepared : StateAudioPlayer(true, R.drawable.play_track, "00:00") {
                override fun isPrepared(): Boolean = true
        }

        class Playing(progress: String) : StateAudioPlayer(true, R.drawable.pause_track, progress) {
                override fun isPrepared(): Boolean = false
        }

        class Paused(progress: String) : StateAudioPlayer(true, R.drawable.play_track, progress) {
                override fun isPrepared(): Boolean = false
        }
}

/*
        DEFAULT,
        PREPARED,
        PLAYING,
        PAUSED
 */