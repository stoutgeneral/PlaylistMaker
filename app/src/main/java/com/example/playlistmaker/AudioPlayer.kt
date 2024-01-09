package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
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
        private const val TRACK = "track"
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 1000L
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

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var playBackIconCondition = 0
    private var handler = Handler(Looper.getMainLooper())

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
            .into(cover)

        if (track.collectionName?.isEmpty() == null) {
            album.visibility = View.GONE
            collectionName.visibility = View.GONE
        } else {
            album.visibility = View.VISIBLE
            collectionName.visibility = View.VISIBLE
            collectionName.text = track.collectionName
        }

        buttonPlay.setOnClickListener {
            playbackControl()
        }

        mediaPlayer.setDataSource(track.previewUrl)
        preparePlayer()
    }


    private fun starTimer(duration: Long) {
        val startTime = System.currentTimeMillis()
        playerRunnable = createUpdateTimerTask(startTime, duration * DELAY)
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
                    val seconds = remainingTime / DELAY
                    segmentTime.text = String.format("%d:%02d", seconds / 60, seconds % 60)
                    handler.postDelayed(this, DELAY)
                } else {
                    pausePlayer()
                }
            }
        }
    }

    private fun preparePlayer() {
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    private fun starPlayer() {
        val count = segmentTime.text
            .toString()
            .replace(":", "")
            .toLong()

        if (count > 0) {
            mediaPlayer.start()
            buttonPlay.setImageResource(R.drawable.pause_track)
            playerState = STATE_PLAYING
            starTimer(count)
        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        buttonPlay.setImageResource(R.drawable.play_track)
        playerState = STATE_PAUSED
        handler.removeCallbacks(playerRunnable)
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> starPlayer()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

}


