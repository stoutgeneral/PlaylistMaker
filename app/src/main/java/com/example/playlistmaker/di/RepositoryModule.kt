package com.example.playlistmaker.di

import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}