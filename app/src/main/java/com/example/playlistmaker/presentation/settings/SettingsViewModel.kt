package com.example.playlistmaker.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.ThemeStatus

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(context = getApplication()) }
    private val shareInteractor by lazy { Creator.provideShareInteractor(context = getApplication()) }

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