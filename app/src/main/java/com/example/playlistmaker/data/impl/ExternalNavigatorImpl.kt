package com.example.playlistmaker.data.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.ExternalNavigator
import com.example.playlistmaker.domain.models.EmailData

class ExternalNavigatorImpl (private val context: Context): ExternalNavigator {

    override fun shareApp(theme: String) {
        val intentShareApp = Intent (Intent.ACTION_SEND)
        intentShareApp.type = "text/plain"
        intentShareApp.putExtra(Intent.EXTRA_TEXT, theme)
        intentShareApp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intentShareApp)
    }

    override fun userAgreement(url: String) {
        val intentUserAgreement = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intentUserAgreement.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intentUserAgreement)
    }

    override fun writeToSupport(data: EmailData) {
        val intentMessageSupport = Intent(Intent.ACTION_SENDTO)
        intentMessageSupport.data = Uri.parse("mailto:")
        intentMessageSupport.putExtra(Intent.EXTRA_EMAIL, arrayOf(data.email))
        intentMessageSupport.putExtra(Intent.EXTRA_SUBJECT, data.theme)
        intentMessageSupport.putExtra(Intent.EXTRA_TEXT, data.text)
        intentMessageSupport.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        context.startActivity(intentMessageSupport)
    }

    override fun sharePlaylist(playlist: String) {
        val intent = Intent(Intent.ACTION_SEND).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, playlist)
        context.startActivity(intent)
    }
}