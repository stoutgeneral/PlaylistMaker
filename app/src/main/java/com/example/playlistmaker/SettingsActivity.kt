package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBack = findViewById<ImageView>(R.id.arrow_back_settings)
        arrowBack.setOnClickListener {
            finish()
        }

        val shareApp = findViewById<FrameLayout>(R.id.share_app)
        shareApp.setOnClickListener {
            val link = getString(R.string.share_link)
            val intentShareApp = Intent (Intent.ACTION_SEND)
            intentShareApp.type = "text/plain"
            intentShareApp.putExtra(Intent.EXTRA_TEXT, link)

            startActivity(intentShareApp)
        }

        val writeToSupport = findViewById<FrameLayout>(R.id.message_to_support)
        writeToSupport.setOnClickListener {
            val themeMessage = getString(R.string.theme_message)
            val message = getString(R.string.message)
            val intentMessageSupport = Intent(Intent.ACTION_SENDTO)
            intentMessageSupport.data = Uri.parse("mailto:")
            intentMessageSupport.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.email)))
            intentMessageSupport.putExtra(Intent.EXTRA_SUBJECT, themeMessage)
            intentMessageSupport.putExtra(Intent.EXTRA_TEXT, message)

            startActivity(intentMessageSupport)
        }

        val userAgreement = findViewById<FrameLayout>(R.id.user_agreement_button)
        userAgreement.setOnClickListener {
            val linkAgreement = getString(R.string.user_agreement_link)
            val intentUserAgreement = Intent(Intent.ACTION_VIEW, Uri.parse(linkAgreement))

            startActivity(intentUserAgreement)
        }
    }
}
