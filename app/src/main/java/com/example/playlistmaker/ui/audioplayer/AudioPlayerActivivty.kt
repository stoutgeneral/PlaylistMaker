package com.example.playlistmaker.ui.audioplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.models.TrackDetails

class AudioPlayerActivivty: AppCompatActivity() {

    companion object {
        private const val TRACK = "track"
    }

    private lateinit var viewModel: AudioPlayerViewModel
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var trackDetails: TrackDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.arrowBackMediaPlayer.setOnClickListener {
            finish()
        }

        // я все еще не сделал трек детаилс

        binding.artistName.text = trackDetails.artistName
        binding.trackName.text = trackDetails.trackName
        binding.countryName.text = trackDetails.country
        binding.genreName.text = trackDetails.primaryGenreName
        binding.durationTime.text = trackDetails.trackTime
        binding.yearRelease.text = trackDetails.releaseYear
        binding.trackTimer.text = "00:30"

        viewModel = ViewModelProvider(this) [AudioPlayerViewModel::class.java]
        viewModel.observePlayState().observe(this) {
            updatePlayButton(it)
        }

        viewModel.observePlayButtonState().observe(this) {
            enablePlayButton(it)
        }

        viewModel.observeSecondState().observe(this) {
            updateTimer(it)
        }

        Glide
            .with(this)
            .load(trackDetails.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(this.findViewById(R.id.track_cover))

        viewModel.preparePlayer(url = trackDetails.previewUrl)

        binding.buttonPlayTrack.setOnClickListener {
            playbackControl()
        }
    }

    private fun updatePlayButton (isPaused: Boolean) {
        if (isPaused) {
            binding.buttonPlayTrack.setImageResource(R.drawable.play_track)
        }
        else {
            binding.buttonPlayTrack.setImageResource(R.drawable.pause_track)
        }
    }

    private fun updateTimer (unitTime: Long) {
        binding.trackTimer.text = String.format("%02d:%02d", unitTime / 60, unitTime % 60)
    }

    private fun enablePlayButton (enabled: Boolean) {
        binding.buttonPlayTrack.isEnabled = enabled
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    private fun playbackControl() {
        val count = binding.trackTimer.text
            .toString()
            .replace(":", "")
            .toLong()
        viewModel.playbackControl(count = count)
    }
}

