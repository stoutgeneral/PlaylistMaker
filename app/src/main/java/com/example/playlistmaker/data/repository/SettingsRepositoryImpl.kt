package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.models.ThemeStatus
import com.example.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val sharePrefs: SharedPreferences) : SettingsRepository {

    companion object {
        const val THEME_STATUS = "DARK_STATE"

        private val map = ThemeStatus.values().associateBy(ThemeStatus::themeState)
        fun getThemeFromInt(type: Int) = map[type]
    }

    private fun installingDarkTheme (condition: ThemeStatus) {
        AppCompatDelegate.setDefaultNightMode(
            when (condition.themeState) {
                1 -> AppCompatDelegate.MODE_NIGHT_NO
                2 -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }

    override fun getThemeStatus(): ThemeStatus {
        val theme = sharePrefs.getInt(THEME_STATUS, -1)
        return getThemeFromInt(theme) ?: ThemeStatus.DEFAULT
    }

    override fun updateThemeStatus(status: ThemeStatus) {
        if (status.themeState > 0) sharePrefs.edit().putInt(THEME_STATUS, status.themeState).apply()
        installingDarkTheme(status)
    }
}