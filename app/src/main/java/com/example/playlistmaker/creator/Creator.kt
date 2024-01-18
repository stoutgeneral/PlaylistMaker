package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.network.RetrofitNetworkCustomer
import com.example.playlistmaker.data.repository.AudioPlayerRepositoryImpl
import com.example.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.example.playlistmaker.data.repository.SettingsRepositoryImpl
import com.example.playlistmaker.data.repository.TrackRepositoryImpl
import com.example.playlistmaker.domain.SearchHistoryInteractor
import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.ShareInteractor
import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.impl.*
import com.example.playlistmaker.domain.repository.AudioPlayerRepository

object Creator {
    fun provideAudioPlayerInteractor(): AudioPlayerRepository {
        return AudioPlayerInteractorImpl(AudioPlayerRepositoryImpl())
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        val repository = TrackRepositoryImpl(RetrofitNetworkCustomer(context = context))
        return TrackInteractorImpl(repository)
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        val repository = SettingsRepositoryImpl(context = context)
        return SettingsInteractorImpl(repository)
    }


    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        val repository = SearchHistoryRepositoryImpl(context = context)
        return SearchHistoryInteractorImpl(repository)
    }

    fun provideShareInteractor(context: Context): ShareInteractor {
        val navigator = ExternalNavigatorImpl(context = context)
        return ShareInteractorImpl(navigator)
    }
}