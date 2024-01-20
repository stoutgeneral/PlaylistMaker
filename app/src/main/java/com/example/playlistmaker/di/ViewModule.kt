package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.main.MainViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        MainViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }


}