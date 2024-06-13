package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivivty
import com.example.playlistmaker.databinding.ActivityRootBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    companion object {
        const val EMPTY_FIELD = ""
    }

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchResultsAdapter: TrackAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var previousRequest: String

    private val viewModel: SearchViewModel by viewModel()
    private var simpleTextWatcher: TextWatcher? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeHistoryState().observe(viewLifecycleOwner) {
            showSearchHistory(it)
        }

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        trackAdapter = TrackAdapter(setAdapterListener())
        searchResultsAdapter = TrackAdapter(setAdapterListener())

        binding.rvTrack.adapter = trackAdapter

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.clearText.visibility = View.VISIBLE
                }
                previousRequest = s?.toString() ?: EMPTY_FIELD
                viewModel.searchDebounce(changedText = previousRequest)

                binding.historyListView.visibility = if (binding.searchBar.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        simpleTextWatcher.let { binding.searchBar.addTextChangedListener(it) }

        binding.clearText.setOnClickListener {
            binding.searchBar.setText(EMPTY_FIELD)
            binding.clearText.visibility = View.GONE
            binding.searchBar.clearFocus()
            clearContent()
        }

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchBar.text.isNotEmpty()) {
                    previousRequest = binding.searchBar.text.toString()
                    viewModel.searchDebounce(binding.searchBar.text.toString())
                }
            }
            false
        }

        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            viewModel.getTrackSearchHistory()
            binding.historyListView.visibility =
                if (hasFocus && binding.searchBar.text.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.placeholderButton.setOnClickListener {
            binding.searchBar.setText(previousRequest)
            viewModel.searchDebounce(previousRequest)
        }

        binding.clearHistory.setOnClickListener {
            viewModel.clearHistory()
            binding.historyListView.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        simpleTextWatcher?.let { binding.searchBar.removeTextChangedListener(it) }
    }

    private fun setAdapterListener(): TrackAdapter.Listener {
        return object : TrackAdapter.Listener {
            override fun onClick(track: Track) {
                viewModel.onClick(track)

                val intent = Intent(requireContext(), AudioPlayerActivivty::class.java)
                intent.putExtra("track", TrackMapper.map(track))
                startActivity(intent)
            }
        }
    }

    private fun render(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Error -> showError()
            is TracksState.Empty -> showEmpty()
            is TracksState.Content -> showContent(state.tracks)
        }
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.rvTrack.visibility = View.GONE
        binding.historyListView.visibility = View.GONE
    }

    private fun showError() {
        binding.placeholderImage.setImageResource(R.drawable.il_connection_error)
        binding.placeholderText.setText(R.string.not_connection)
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.rvTrack.visibility = View.GONE
        binding.historyListView.visibility = View.GONE

        binding.placeholderButton.visibility = View.VISIBLE
    }


    private fun showEmpty() {
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.placeholderImage.setImageResource(R.drawable.il_nothing_found)
        binding.placeholderText.setText(R.string.not_found)
        binding.placeholderButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showContent(newTracksList: List<Track>) {
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(newTracksList)
        trackAdapter.notifyDataSetChanged()

        binding.rvTrack.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.historyListView.visibility = View.GONE
    }

    private fun clearContent() {
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun showSearchHistory(tracks: ArrayList<Track>) {
        searchResultsAdapter.tracks = tracks
        binding.rvHistory.adapter = searchResultsAdapter
        searchResultsAdapter.notifyDataSetChanged()

        binding.rvHistory.visibility = View.VISIBLE
        binding.rvTrack.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.historyListView.visibility = View.VISIBLE
    }
}