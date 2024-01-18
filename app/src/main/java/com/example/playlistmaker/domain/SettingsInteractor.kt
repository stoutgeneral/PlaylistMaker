package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.ThemeStatus

interface SettingsInteractor {
    fun getThemeStatus(): ThemeStatus
    fun updateThemeStatus(status: ThemeStatus)
}