package com.example.playlistmaker.data

import com.example.playlistmaker.domain.models.EmailData

interface ExternalNavigator {

    fun shareApp(theme: String)
    fun userAgreement(url: String)
    fun writeToSupport(email: EmailData)
    fun sharePlaylist (playlist: String)
}