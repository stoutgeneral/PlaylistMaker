package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentDetailsPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.library.DetailsPlaylistFragmentViewModel
import com.example.playlistmaker.presentation.models.PlaylistStateTrack
import com.example.playlistmaker.util.ConvertTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.StringBuilder

class DetailsPlaylistFragment : Fragment() {

    companion object {
        private const val TRACK = "track"
        private const val PLAYLIST_ID = "playlistId"
        private const val CLICK_DEBOUNCE_ML = 1000L
        fun createArgs(playlistId: Int?): Bundle = bundleOf(PLAYLIST_ID to playlistId)
    }

    private lateinit var binding: FragmentDetailsPlaylistBinding
    private val viewModel: DetailsPlaylistFragmentViewModel by viewModel()

    private var playlistTrack: List<Track> = listOf()
    private var numTrack: String = ""
    private var isClickAllowed = true
    private lateinit var playlistTrackAdapter: LibraryPlaylistTrackAdapter
    private lateinit var playlistTrackAdapterL: LibraryPlaylistTrackAdapter.Listener

    private lateinit var bottomSheetTracks: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetMenu: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailsPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
        isClickAllowed = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
        if (playlistId > 0) {
            viewModel.getPlaylist(playlistId)
            viewModel.getPlaylistTrack(playlistId)
        } else {
            findNavController().popBackStack()
        }

        viewModel.observePlaylist().observe(viewLifecycleOwner) { initPlaylist(it) }
        viewModel.observeTrack().observe(viewLifecycleOwner) { initPlaylistTrack(it) }

        playlistTrackAdapterL = object : LibraryPlaylistTrackAdapter.Listener {
            override fun onClickLong(trackInt: Int): Boolean {
                showDeleteTrackDialog(trackInt)
                return true
            }

            override fun onClickShort(track: Track) {
                if (clickDebounce()) {
                    val bundle = Bundle()
                    bundle.putParcelable(TRACK, track)
                    findNavController().navigate(
                        R.id.actionGlobalPlayer,
                        bundle
                    )
                }
            }
        }
        playlistTrackAdapter = LibraryPlaylistTrackAdapter(playlistTrackAdapterL)
        binding.rvPlaylistTrack.adapter = playlistTrackAdapter

        bottomSheetMenu = BottomSheetBehavior.from(binding.bottomSheetPlaylistMenu)
        bottomSheetMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> binding.playlistOverlay.visibility =
                        View.GONE

                    else -> binding.playlistOverlay.visibility = View.VISIBLE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        bottomSheetTracks = BottomSheetBehavior.from(binding.bottomSheetPlaylistTrack)
        bottomSheetTracks.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}

        })
    }

    private fun initPlaylist(playlist: Playlist) {
        binding.playlistName.text = playlist.name
        binding.playlistMenuName.text = playlist.name
        binding.playlistDescription.text = playlist.description
        binding.playlistMenuDescription.text = playlist.description

        numTrack = resources.getQuantityString(
            R.plurals.track_counter,
            playlist.tracks.size,
            playlist.tracks.size
        )
        val numMinStrings: String = resources.getQuantityString(
            R.plurals.mins_counter,
            ConvertTime.convertToMin(playlist.trackTimeMillis).toInt(),
            ConvertTime.convertToMin(playlist.trackTimeMillis).toInt()
        )

        val stringsBulder = StringBuilder()
        stringsBulder.append(numMinStrings)
        stringsBulder.append(" \u2022 ")
        stringsBulder.append(numTrack)
        binding.playlistTimeQuantity.text = stringsBulder.toString()

        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop())
            .into(binding.detailsPlaylistCover)

        Glide.with(this)
            .load(playlist.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop())
            .into(binding.playlistMenuCover)

        binding.detailsPlaylistCover.contentDescription =
            "${playlist.name} : + ${playlist.description}"

        binding.btPlaylistShare.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }
        binding.btPlaylistMenu.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
        binding.btShareMenu.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist()
        }
        binding.btDeletePlaylist.setOnClickListener {
            bottomSheetMenu.state = BottomSheetBehavior.STATE_HIDDEN
            showDeletePlaylistDialog()
        }
        binding.btEditPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_detailsPlaylistFragment_to_editPlaylistFragment,
                EditPlaylistFragment.createArgs(playlist.id)
            )
        }

        binding.back.setOnClickListener {
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

        binding.btPlaylistShare.post {
            bottomSheetTracks.apply {
                peekHeight =
                    binding.root.height - binding.btPlaylistShare.bottom - resources.getDimensionPixelSize(
                        R.dimen.high_padding
                    )
                state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }
    }

    private fun initPlaylistTrack(state: PlaylistStateTrack) {
        when (state) {
            is PlaylistStateTrack.Empty -> {
                showEmpty()
                playlistTrack = listOf()
            }
            is PlaylistStateTrack.Content -> {
                showContent(state.track)
                playlistTrack = state.track
            }
            else -> {}
        }
    }

    private fun showEmpty() {
        binding.rvPlaylistTrack.visibility = View.GONE
        binding.playlistEmpty.visibility = View.VISIBLE
    }

    private fun showContent(track: List<Track>) {
        binding.playlistEmpty.visibility = View.GONE

        playlistTrackAdapter.tracks.clear()
        playlistTrackAdapter.tracks.addAll(track)
        playlistTrackAdapter.notifyDataSetChanged()

        binding.rvPlaylistTrack.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_ML)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun showDeleteTrackDialog(trackId: Int) {
        MaterialAlertDialogBuilder(requireContext(), R.style.AllertDialog)
            .setTitle(R.string.track_delete)
            .setMessage(R.string.track_delete_question)
            .setNegativeButton(R.string.no) { _, _ ->
            }
            .setPositiveButton(R.string.yes) { _, _ ->
                val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
                if (playlistId > 0)
                    viewModel.deletePlaylistTrack(playlistId, trackId)
            }.show()
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext(), R.style.AllertDialog)
            .setTitle(R.string.playlist_delete)
            .setMessage(R.string.playlist_alert_message)
            .setNegativeButton(R.string.no) { _, _ ->
            }
            .setPositiveButton(R.string.yes) { _, _ ->
                val playlistId = requireArguments().getInt(PLAYLIST_ID, 0)
                if (playlistId > 0) {
                    viewModel.deletePlaylist(playlistId)
                    findNavController().popBackStack()
                }
            }.show()
    }

    private fun sharePlaylist() {
        if (playlistTrack.isEmpty()) {
            Toast.makeText(
                requireContext(),
                getString(R.string.playlist_not_track_to_share),
                Toast.LENGTH_LONG
            ).show()
        } else {
            val sb = StringBuilder()
            sb.append("${binding.playlistName.text}\n${binding.playlistDescription.text}\n${numTrack}\n\n") // ???

            val tracksString = buildString {
                for (i in playlistTrack.indices) {
                    append(i + 1)
                    append(". ")
                    append(playlistTrack[i].artistName)
                    append(" - ")
                    append(playlistTrack[i].trackName)
                    append(" (")
                    append(playlistTrack[i].trackTimeMillis?.let {
                        ConvertTime.convertToMinAndSec(
                            it
                        )
                    })
                    append(")")
                    append("\n")
                }
            }
            sb.append(tracksString)

            viewModel.shareAppPlaylist(sb.toString())
        }
    }
}
