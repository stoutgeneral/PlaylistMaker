package com.example.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.ThemeStatus

class App : Application() {

    private lateinit var themeSettings: ThemeStatus
    private val themeInteractor by lazy { Creator.provideSettingsInteractor(context = applicationContext) }

    override fun onCreate() {
        super.onCreate()

        themeSettings = themeInteractor.getThemeStatus()
        themeInteractor.updateThemeStatus(themeSettings)
    }
}