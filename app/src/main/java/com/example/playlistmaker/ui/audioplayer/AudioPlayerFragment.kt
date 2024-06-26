package com.example.playlistmaker.ui.audioplayer

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentAudioPlayerBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.models.PlaylistState
import com.example.playlistmaker.presentation.models.PlaylistStateTrack
import com.example.playlistmaker.util.ConvertTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.Instant
import java.util.Date
import java.util.Locale

class AudioPlayerFragment : Fragment() {

    companion object {
        private const val TRACK = "track"
        private const val CLICK_DEBOUNCE_DM = 1000L
    }

    private lateinit var binding: FragmentAudioPlayerBinding
    private lateinit var track: Track
    private val viewModel: AudioPlayerViewModel by viewModel()
    private var playlistIsClickAllowed = true

    private lateinit var timeInterval: String

    private val playlistAdapter = PlaylistAdapter {
        addTrackInPlaylist(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAudioPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //binding = FragmentAudioPlayerBinding.inflate(layoutInflater)

        track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) arguments?.getParcelable(
            TRACK,
            Track::class.java
        )!! else arguments?.getParcelable(TRACK)!!

        binding.arrowBackMediaPlayer.setOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )

        viewModel.observePlayState().observe(viewLifecycleOwner) {
            this.timeInterval = it.progress
            binding.buttonPlayTrack.isEnabled = it.checkingButtonStatus
            binding.buttonPlayTrack.setImageResource(it.buttonState)
            binding.trackTimer.text = it.progress
        }

        viewModel.observeFavoriteState().observe(viewLifecycleOwner) { isFavorite ->
            if (isFavorite) {
                binding.buttonFavorites.setImageResource(R.drawable.added_favorite)
            } else {
                binding.buttonFavorites.setImageResource(R.drawable.add_favorites)
            }
            track.isFavorite = isFavorite // !!!
        }

        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            renderPlaylist(it)
        }

        binding.recyclerPlaylistsInPLayer.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPlaylistsInPLayer.adapter = playlistAdapter

        binding.artistName.text = track.artistName
        binding.trackName.text = track.trackName
        binding.countryName.text = track.country
        binding.genreName.text = track.primaryGenreName
        binding.durationTime.text = ConvertTime.convertToMinAndSec(track.trackTimeMillis)
        binding.albumName.text = track.collectionName
        binding.yearRelease.text = convertToYear(track.releaseDate)
        binding.trackTimer.text = "00:30"

        if (track.isFavorite) binding.buttonFavorites.setImageResource(R.drawable.added_favorite)

        track.previewUrl?.let { viewModel.preparePlayer(url = it) } // ..

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

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.overlay.visibility = View.GONE
                    else -> binding.overlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        viewModel.observeAddedPlaylistState().observe(viewLifecycleOwner) { addedToPlaylist ->
            when (addedToPlaylist) {
                is PlaylistStateTrack.Respond -> renderToast(addedToPlaylist.name, false)
                is PlaylistStateTrack.Added -> {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    renderToast(addedToPlaylist.name, true)
                }

                else -> {} // ..
            }
        }

        binding.addPlaylistButton.setOnClickListener {
            viewModel.getPlaylists()
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.halfExpandedRatio = 0.65F
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_audioPlayerFragment_to_createPlaylistFragment)

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
            .into(binding.trackCover)
    }

    private fun convertToYear(releaseDate: String?): String? {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(
            Date.from(
                Instant.parse(
                    releaseDate
                )
            )
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.onPause()
    }

    override fun onResume() {
        super.onResume()
        viewModel.onPause()
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
                requireContext(),
                getString(R.string.playlist_track_has_been_added, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_track_has_already_add, playlistName),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

