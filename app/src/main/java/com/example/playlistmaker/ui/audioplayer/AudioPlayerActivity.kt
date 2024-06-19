package com.example.playlistmaker.ui.audioplayer

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.models.TrackDetails
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val TRACK = "track"
    }

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var trackDetails: TrackDetails
    private val viewModel: AudioPlayerViewModel by viewModel()

    private lateinit var timeInterval: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackDetails = getSerializable(TRACK, TrackDetails::class.java)

        binding.arrowBackMediaPlayer.setOnClickListener {
            finish()
        }

        binding.artistName.text = trackDetails.artistName
        binding.trackName.text = trackDetails.trackName
        binding.countryName.text = trackDetails.country
        binding.genreName.text = trackDetails.primaryGenreName
        binding.durationTime.text = trackDetails.trackTime
        binding.albumName.text = trackDetails.collectionName
        binding.yearRelease.text = trackDetails.releaseYear
        binding.trackTimer.text = "00:30"

        viewModel.observePlayState().observe(this) {
            timeInterval = it.progress
            binding.buttonPlayTrack.isEnabled = it.checkingButtonStatus
            binding.buttonPlayTrack.setImageResource(it.buttonState)
            binding.trackTimer.text = it.progress
        }

        viewModel.preparePlayer(url = trackDetails.previewUrl)

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        Glide
            .with(this)
            .load(trackDetails.getCoverArtwork())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(this.findViewById(R.id.track_cover))
    }

    private fun updateTimer(unitTime: Long) {
        binding.trackTimer.text = String.format("%02d:%02d", unitTime / 60, unitTime % 60)
    }

    private fun enablePlayButton(enabled: Boolean) {
        binding.buttonPlayTrack.isEnabled = enabled
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    /*private fun playbackControl() {
        val count = binding.trackTimer.text
            .toString()
            .replace(":", "")
            .toLong()
        viewModel.playbackControl(count = count)
    }*/

    private fun <T : Serializable?> getSerializable(name: String, clazz: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(name, clazz)!!
        else
            intent.getSerializableExtra(name) as T
    }
}

