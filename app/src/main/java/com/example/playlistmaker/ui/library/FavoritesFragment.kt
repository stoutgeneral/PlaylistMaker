package com.example.playlistmaker.ui.library

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.playlistmaker.databinding.FragmentFavoritesBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.library.FavoritesFragmentViewModel
import com.example.playlistmaker.presentation.models.FavoriteState
import com.example.playlistmaker.ui.audioplayer.AudioPlayerFragment
import com.example.playlistmaker.ui.search.TrackAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoritesFragment : Fragment() {

    companion object {
        fun newInstance() : FavoritesFragment {
            return FavoritesFragment()
        }
        private const val CLICK_DEBOUNCE = 1000L
    }

    private val viewModel: FavoritesFragmentViewModel by viewModel()
    private lateinit var binding: FragmentFavoritesBinding

    private var isClickAllowed = true
    private val trackAdapter = TrackAdapter {
        switchToPlayer(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeFavoriteState().observe(viewLifecycleOwner) {
            render(it)
        }
        binding.rvFavorites.adapter = trackAdapter
    }

    override fun onResume() {
        super.onResume()
        viewModel.getTracks()
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Empty -> showEmpty()
            is FavoriteState.Content -> showContent(state.tracks)
        }
    }

    private fun showEmpty() { // пустой список "Избранное"
        binding.tvEmptyFavorites.visibility = View.VISIBLE
        binding.ivEmptyFavorites.visibility = View.VISIBLE
        binding.rvFavorites.visibility = View.GONE
    }

    private fun showContent(tracks: List<Track>) {
        binding.tvEmptyFavorites.visibility = View.GONE
        binding.ivEmptyFavorites.visibility = View.GONE

        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()

        binding.rvFavorites.visibility = View.VISIBLE
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE)
                isClickAllowed = true
            }
        }
        return current
    }

    private fun switchToPlayer(track: Track) {
        if (clickDebounce()) {
            val bundle = Bundle()
            bundle.putParcelable("track", track)

            findNavController().navigate(R.id.actionGlobalPlayer, bundle)
        }
    }

}