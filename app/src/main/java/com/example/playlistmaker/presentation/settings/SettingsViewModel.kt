package com.example.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.SettingsInteractor
import com.example.playlistmaker.domain.ShareInteractor
import com.example.playlistmaker.domain.models.ThemeStatus

class SettingsViewModel(private val settingsInteractor: SettingsInteractor, private val shareInteractor: ShareInteractor) : ViewModel() {


    fun shareApp(url: String) {
        shareInteractor.shareApp(link = url)
    }

    fun writeToSupport(email: String, theme: String, text: String) {
        shareInteractor.writeToSupport(email = email, theme = theme, text = text)
    }

    fun openUserAgreement(url: String) {
        shareInteractor.openUserAgreement(link = url)
    }

    fun upThemeSetting(condition: Boolean) {
        if (condition) {
            settingsInteractor.updateThemeStatus(ThemeStatus.DARK)
        } else {
            settingsInteractor.updateThemeStatus(ThemeStatus.LIGHT)
        }
    }
}