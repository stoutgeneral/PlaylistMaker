package com.example.playlistmaker.di

import com.example.playlistmaker.data.mapper.FavoriteMapper
import com.example.playlistmaker.data.mapper.PlaylistMapper
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.FavoriteRepositoryImpl
import com.example.playlistmaker.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.domain.repository.FavoriteRepository
import com.example.playlistmaker.domain.repository.PlaylistRepository
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.example.playlistmaker.domain.repository.SettingsRepository
import com.example.playlistmaker.domain.repository.TrackRepository
import org.koin.dsl.module
import kotlin.math.sin

val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    factory<AudioPlayerRepository> {
        AudioPlayerRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    factory { FavoriteMapper() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    factory { PlaylistMapper() }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get())
    }
}