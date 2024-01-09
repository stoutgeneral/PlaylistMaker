package com.example.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class AudioPlayer : AppCompatActivity() {
    companion object {
        const val TRACK = "track"
    }

    private lateinit var track: Track
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var time: TextView
    private lateinit var collectionName: TextView
    private lateinit var releaseDate: TextView
    private lateinit var primaryGenreName: TextView
    private lateinit var country: TextView
    private lateinit var cover: ImageView
    private lateinit var album: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

        val buttonBack = findViewById<ImageView>(R.id.arrow_back_media_player)
        buttonBack.setOnClickListener {
            finish()
        }

        val dataTrack = intent.getStringExtra(TRACK)
        track = Gson().fromJson(dataTrack, Track::class.java)

        val yearRelease = Date.from(Instant.parse(track.releaseDate))
        trackName = findViewById(R.id.track_name)
        artistName = findViewById(R.id.artist_name)
        time = findViewById(R.id.duration_time)
        releaseDate = findViewById(R.id.year_release)
        primaryGenreName = findViewById(R.id.genre_name)
        country = findViewById(R.id.country_name)
        cover = findViewById(R.id.track_cover)
        album = findViewById(R.id.album)
        collectionName = findViewById(R.id.album_name)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        releaseDate.text = SimpleDateFormat("yyyy",Locale.getDefault()).format(yearRelease)
        time.text = track.getSimpleDateFormat(track.trackTime)

        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(cover)

        if (track.collectionName?.isEmpty() == null) {
            album.visibility = View.GONE
            collectionName.visibility = View.GONE
        } else {
            album.visibility = View.VISIBLE
            collectionName.visibility = View.VISIBLE
            collectionName.text = track.collectionName
        }
    }
}