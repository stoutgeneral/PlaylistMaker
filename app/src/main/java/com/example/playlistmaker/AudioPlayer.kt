package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AudioPlayer: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<ImageView>(R.id.arrow_back_media_player)

        // Возврат в меню Поиск
        buttonBack.setOnClickListener {
            this.finish()
        }

        val track = intent.getSerializableExtra("track") as Track

        val trackName = findViewById<TextView>(R.id.track_name)

        val artistName = findViewById<TextView>(R.id.artist_name)


        val trackTime = findViewById<TextView>(R.id.duration_time)


        val collectionName = findViewById<TextView>(R.id.album_name)


        val releaseDate = findViewById<TextView>(R.id.year_release)


        val genreName = findViewById<TextView>(R.id.genre_name)


        val country = findViewById<TextView>(R.id.country_name)



    }

}