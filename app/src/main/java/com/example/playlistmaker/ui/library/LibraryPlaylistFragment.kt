package com.example.playlistmaker.ui.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.presentation.library.FragmentLibraryPlaylistViewModel
import com.example.playlistmaker.presentation.models.PlaylistState
import com.example.playlistmaker.ui.audioplayer.PlaylistAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class LibraryPlaylistFragment : Fragment() {

    companion object {
        fun newInstance(): LibraryPlaylistFragment {
            return LibraryPlaylistFragment()
        }
    }

    private val viewModel: FragmentLibraryPlaylistViewModel by viewModel()
    private lateinit var binding: FragmentPlaylistBinding
    private val playlistAdapter = LibraryPlaylistAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observePlaylistState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.rvPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylists.adapter = playlistAdapter
        binding.btNewPlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_mediaFragment_to_createPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPlaylist()
    }

    private fun render(state: PlaylistState) {
        when (state) {
            is PlaylistState.Empty -> showEmpty()
            is PlaylistState.Content -> showContent(state.playlists)
            else -> {}
        }
    }

    private fun showEmpty() {
        binding.rvPlaylists.visibility = View.GONE
        binding.ivEmptyPlaylist.visibility = View.VISIBLE
        binding.tvEmptyPlaylist.visibility = View.VISIBLE
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.ivEmptyPlaylist.visibility = View.GONE
        binding.tvEmptyPlaylist.visibility = View.GONE

        playlistAdapter.playlists.clear()
        playlistAdapter.playlists.addAll(playlists)
        playlistAdapter.notifyDataSetChanged()

        binding.rvPlaylists.visibility = View.VISIBLE
    }
}