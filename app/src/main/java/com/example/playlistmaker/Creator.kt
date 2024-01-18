package com.example.playlistmaker

import com.example.playlistmaker.data.AudioPlayerUseCase
import com.example.playlistmaker.domain.AudioPlayerInteractor
import com.example.playlistmaker.domain.impl.AudioPlayerUseCaseImpl

object Creator {
    fun provideAudioPlayerInteractor () : AudioPlayerInteractor {
        return AudioPlayerUseCaseImpl(AudioPlayerUseCase())
    }
}