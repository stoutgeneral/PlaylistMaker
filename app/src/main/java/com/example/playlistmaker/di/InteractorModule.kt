package com.example.playlistmaker.di

import com.example.playlistmaker.domain.SearchHistoryInteractor
import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.ShareInteractor
import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.impl.*
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import org.koin.dsl.module

val interactorModule = module {
    single<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }


    factory<AudioPlayerRepository> {
        AudioPlayerInteractorImpl(get())
    }


    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<ShareInteractor> {
        ShareInteractorImpl(get())
    }
}