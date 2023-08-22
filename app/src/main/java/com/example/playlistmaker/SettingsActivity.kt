package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val arrowBack =  findViewById<ImageView>(R.id.arrow_back)

        arrowBack.setOnClickListener {
            val arrowBackIntent = Intent (this, MainActivity::class.java)
            startActivity(arrowBackIntent)
        }
    }
}