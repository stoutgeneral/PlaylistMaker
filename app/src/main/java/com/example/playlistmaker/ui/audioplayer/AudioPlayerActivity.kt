package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.util.Date
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val TRACK = "track"
    }

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track
    private val viewModel: AudioPlayerViewModel by viewModel()

    private lateinit var timeInterval: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getSerializable(TRACK, Track::class.java)

        binding.arrowBackMediaPlayer.setOnClickListener {
            finish()
        }

        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.countryName.text = track.country
        binding.genreName.text = track.primaryGenreName
        binding.durationTime.text = track.trackTime
        binding.albumName.text = track.collectionName
        binding.yearRelease.text = convertToYear(track.releaseDate)
        binding.trackTimer.text = "00:30"

        viewModel.observePlayState().observe(this) {
            timeInterval = it.progress
            binding.buttonPlayTrack.isEnabled = it.checkingButtonStatus
            binding.buttonPlayTrack.setImageResource(it.buttonState)
            binding.trackTimer.text = it.progress
        }

        viewModel.observeFavoriteState().observe(this) { isFavorite ->
            if (isFavorite) {
                binding.buttonFavorites.setImageResource(R.drawable.added_favorite)
            } else {
                binding.buttonFavorites.setImageResource(R.drawable.add_favorites)
            }
        }

        viewModel.preparePlayer(url = track.previewUrl)

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.buttonFavorites.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        Glide
            .with(this)
            .load(track.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(this.findViewById(R.id.track_cover))
    }

    private fun convertToYear(releaseDate: String?): String? {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date.from(Instant.parse(releaseDate)))
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
}

