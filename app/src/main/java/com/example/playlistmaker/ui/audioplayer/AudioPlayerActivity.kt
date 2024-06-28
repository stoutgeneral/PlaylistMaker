package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.models.PlaylistState
import com.example.playlistmaker.presentation.models.PlaylistStateTrack
import com.example.playlistmaker.ui.library.CreatePlaylistsFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Serializable
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.util.Date
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    companion object {
        private const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DM = 1000L
    }

    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track
    private val viewModel: AudioPlayerViewModel by viewModel()
    private var playlistIsClickAllowed = true

    private lateinit var timeInterval: String

    private val playlistAdapter = PlaylistAdapter {
        addTrackInPlaylist(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = getSerializable(TRACK, Track::class.java)

        binding.arrowBackMediaPlayer.setOnClickListener {
            finish()
        }

        viewModel.observePlayState().observe(this) {
            this.timeInterval = it.progress
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
            track.isFavorite = !isFavorite
        }

        viewModel.observePlaylistState().observe(this) {
            renderPlaylist(it)
        }

        binding.recyclerPlaylistsInPLayer.layoutManager = LinearLayoutManager(this)
        binding.recyclerPlaylistsInPLayer.adapter = playlistAdapter

        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.countryName.text = track.country
        binding.genreName.text = track.primaryGenreName
        binding.durationTime.text = track.trackTime
        binding.albumName.text = track.collectionName
        binding.yearRelease.text = convertToYear(track.releaseDate)
        binding.trackTimer.text = "00:30"

        if (track.isFavorite) binding.buttonFavorites.setImageResource(R.drawable.added_favorite)

        viewModel.preparePlayer(url = track.previewUrl)

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.buttonFavorites.setOnClickListener {
            viewModel.onFavoriteClicked(track)
        }

        val bottomSheetContainer = binding.bottomSheetPlaylist
        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.observeAddedPlaylistState().observe(this) {addedToPlaylist ->
            when (addedToPlaylist) {
                is PlaylistStateTrack.Respond -> renderToast(addedToPlaylist.name, false)
                is PlaylistStateTrack.Added -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    renderToast(addedToPlaylist.name, true)
                }
            }
        }

        binding.addPlaylistButton.setOnClickListener {// кнопка, которая добавляет трек в плейлист. Она работает, фрагмент вызывается.
            viewModel.getPlaylists()
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.halfExpandedRatio = 0.65F
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.buttonNewPlaylist.setOnClickListener {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.playlist_fragment_container, CreatePlaylistsFragment.newInstance(true))
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.mainPlayerScreen.visibility = View.GONE // ??
            binding.overlay.visibility = View.GONE
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

    private fun addTrackInPlaylist(playlist: Playlist) {
        if (clickDebounce()) {
            viewModel.addTrackInPlaylist(playlist, track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = playlistIsClickAllowed
        if (playlistIsClickAllowed) {
            playlistIsClickAllowed = false
            lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DM)
                playlistIsClickAllowed = true
            }
        }
        return current
    }

    private fun renderPlaylist(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Content -> showContent(state.playlists)
            else -> {}
        }
    }

    private fun showEmpty() {
        binding.recyclerPlaylistsInPLayer.visibility = View.GONE
    }

    private fun showContent(playlists: List<Playlist>) {
        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()
        binding.recyclerPlaylistsInPLayer.visibility = View.VISIBLE
    }

    private fun renderToast(playlistName: String?, added: Boolean) {
        if (added) {
            Toast.makeText(
                this,
                getString(R.string.playlist_track_has_been_added, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                this,
                getString(R.string.playlist_track_has_already_add, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

