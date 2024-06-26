package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.library.FavoritesFragmentViewModel
import com.example.playlistmaker.presentation.library.FragmentLibraryPlaylistViewModel
import com.example.playlistmaker.presentation.root.RootViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get())
    }

    viewModel {
        RootViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        FavoritesFragmentViewModel(get())
    }

    viewModel {
        FragmentLibraryPlaylistViewModel()
    }

}