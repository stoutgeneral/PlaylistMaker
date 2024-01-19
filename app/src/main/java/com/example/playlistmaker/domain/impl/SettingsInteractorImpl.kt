package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.models.ThemeStatus
import com.example.playlistmaker.domain.repository.SettingsRepository

class SettingsInteractorImpl (private val settingsRepository: SettingsRepository) : SettingsInteractor {
    override fun getThemeStatus(): ThemeStatus {
        return settingsRepository.getThemeStatus()
    }

    override fun updateThemeStatus(status: ThemeStatus) {
        settingsRepository.updateThemeStatus(status)
    }

}