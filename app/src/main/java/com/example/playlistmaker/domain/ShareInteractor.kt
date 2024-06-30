package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.models.Playlist

interface ShareInteractor {
    fun shareApp (link: String)
    fun openUserAgreement (link: String)
    fun writeToSupport (email: String, theme: String, text: String)
    fun sharePlaylist(playlist: String)
}