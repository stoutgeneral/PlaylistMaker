package com.example.playlistmaker.domain

interface ShareInteractor {
    fun shareApp (link: String)
    fun openUserAgreement (link: String)
    fun writeToSupport (email: String, theme: String, text: String)
}