package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.data.ExternalNavigator
import com.example.playlistmaker.domain.ShareInteractor
import com.example.playlistmaker.domain.models.EmailData

class ShareInteractorImpl (private val externalNavigator: ExternalNavigator) : ShareInteractor {

    override fun shareApp(link: String) {
        externalNavigator.shareApp(theme = link)
    }

    override fun openUserAgreement(link: String) {
        externalNavigator.userAgreement(url = link)
    }

    override fun writeToSupport(email: String, theme: String, text: String) {
        externalNavigator.writeToSupport(EmailData(
            email = email,
            theme = theme,
            text = text
        ))
    }
}