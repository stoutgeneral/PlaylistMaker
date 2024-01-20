package com.example.playlistmaker.presentation.main

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.SettingsInteractor

class MainViewModel (private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun setAppTheme() {
        val systemSettings = settingsInteractor.getThemeStatus()
        settingsInteractor.updateThemeStatus(systemSettings)
    }
}