package com.example.playlistmaker.di

import com.example.playlistmaker.domain.FavoriteInteractor
import com.example.playlistmaker.domain.SearchHistoryInteractor
import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.ShareInteractor
import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.impl.*
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import org.koin.dsl.module

val interactorModule = module {
    factory<TrackInteractor> {
        TrackInteractorImpl(get())
    }

    factory<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }


    single<AudioPlayerRepository> {
        AudioPlayerInteractorImpl(get())
    }


    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<ShareInteractor> {
        ShareInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}