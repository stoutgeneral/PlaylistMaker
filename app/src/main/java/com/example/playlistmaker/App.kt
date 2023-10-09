package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    companion object {
        const val SHARE_PREFS = "sharePrefs"
        const val THEME_KEY = "themeKey"
    }

    var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}