package com.example.playlistmaker

import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.domain.PlayerInteractor
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl

object Creator {
     fun providePlayerInteractor () : PlayerInteractor {
         return PlayerInteractorImpl(PlayerRepositoryImpl())
     }
}