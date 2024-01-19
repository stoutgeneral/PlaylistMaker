package com.example.playlistmaker.domain.repository

import com.example.playlistmaker.domain.models.ThemeStatus

interface SettingsRepository {
    fun getThemeStatus(): ThemeStatus
    fun updateThemeStatus(status: ThemeStatus)
}