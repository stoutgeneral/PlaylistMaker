package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    companion object {
        const val SHARE_PREFS = "sharePrefs"
        const val THEME_KEY = "themeKey"
    }

    var darkTheme = false
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPreferences = getSharedPreferences(SHARE_PREFS, MODE_PRIVATE)
        darkTheme = sharedPreferences.getBoolean(THEME_KEY, false)
        installTheme(darkTheme)
    }

    fun installTheme(darkThemeEnabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun switchTheme (darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        sharedPreferences.edit()
            .putBoolean(THEME_KEY, darkThemeEnabled)
            .apply()

        installTheme(darkThemeEnabled)
    }
}