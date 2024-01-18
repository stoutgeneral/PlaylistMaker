package com.example.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.AudioPlayerInteractor
import com.example.playlistmaker.domain.AudioPlayerRepository
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class AudioPlayer : AppCompatActivity() {

    companion object {
        private const val TRACK = "track"
        private const val DELAY_MILLIS = 1000L
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

    private lateinit var buttonPlay: ImageButton
    private lateinit var segmentTime: TextView
    private lateinit var playerRunnable: Runnable

    private val audioPlayerInteractor: AudioPlayerInteractor = Creator.provideAudioPlayerInteractor()
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)

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
        buttonPlay = findViewById(R.id.button_play_track)
        segmentTime = findViewById(R.id.track_timer)

        trackName.text = track.trackName
        artistName.text = track.artistName
        primaryGenreName.text = track.primaryGenreName
        country.text = track.country
        releaseDate.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(yearRelease)
        time.text = track.getSimpleDateFormat(track.trackTime)
        segmentTime.text = "00:30"

        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(this.findViewById(R.id.track_cover))

        buttonPlay.setOnClickListener {
            playbackControl()
        }

        buttonPlay.isEnabled = false
        audioPlayerInteractor.preparePlayer(track.previewUrl) { state ->
            when (state) {
                State.PREPARED -> buttonPlay.isEnabled = true
                else -> {}
            }
        }

        val buttonBack = findViewById<ImageView>(R.id.arrow_back_media_player)
        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        buttonPlay.setImageResource(R.drawable.play_track)
        handler.removeCallbacksAndMessages(null)
    }

    private fun playbackControl() {
        val count = segmentTime.text
            .toString()
            .replace(":", "")
            .toLong()

        if (count > 0) {
            audioPlayerInteractor.changingPlayer { state ->
                when (state) {
                    State.PLAYING -> {
                        buttonPlay.setImageResource(R.drawable.pause_track)
                        startTimer(count)
                    }
                    State.PREPARED, State.PAUSED -> {
                        buttonPlay.setImageResource(R.drawable.play_track)
                        handler.removeCallbacks(playerRunnable)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun startTimer(duration: Long) {
        val startTime = System.currentTimeMillis()
        playerRunnable = createUpdateTimerTask(startTime, duration * DELAY_MILLIS)
        handler.post(playerRunnable)
    }

    private fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                // Сколько прошло времени с момента запуска таймера
                val elapsedTime = System.currentTimeMillis() - startTime
                // Сколько осталось до конца
                val remainingTime = duration - elapsedTime

                if (remainingTime > 0) {
                    // Если всё ещё отсчитываем секунды —
                    // обновляем UI и снова планируем задачу
                    val seconds = remainingTime / DELAY_MILLIS
                    segmentTime.text = String.format("%d:%02d", seconds / 60, seconds % 60)
                    handler.postDelayed(this, DELAY_MILLIS)
                } else {
                    pausePlayer()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        audioPlayerInteractor.stoppingPlayer()
        handler.removeCallbacksAndMessages(null)
    }
}


