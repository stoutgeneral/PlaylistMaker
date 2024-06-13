package com.example.playlistmaker.presentation.root

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.SettingsInteractor

class RootViewModel (private val settingsInteractor: SettingsInteractor) : ViewModel() {

    fun setAppTheme() {
        val systemSettings = settingsInteractor.getThemeStatus()
        settingsInteractor.updateThemeStatus(systemSettings)
    }
}