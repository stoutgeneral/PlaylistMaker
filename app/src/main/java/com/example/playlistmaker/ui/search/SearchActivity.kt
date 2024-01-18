package com.example.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.mapper.TrackMapper
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivivty

class SearchActivity : AppCompatActivity() { // есть вероятность, что я намудрил с методами и ресайклерами.
    companion object {
        const val SHARED_PREFERENCES = "SHARED_PREFERENCES"
        const val TRACK = "track"
    }

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var lastSearch: String

    private var searchTextWatcher: TextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory()) [SearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        viewModel.observeHistoryState().observe(this) {
            showSearchHistory(it)
        }

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        trackAdapter = TrackAdapter(setAdapterListener())
        searchAdapter = TrackAdapter(setAdapterListener())

        binding.rvTrack.adapter = trackAdapter

        // Выход из экрана настроек
        binding.arrowBackSearch.setOnClickListener {
            finish()
        }

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    binding.resetText.visibility = View.VISIBLE
                }
                lastSearch = s?.toString() ?: ""
                viewModel.searchDebounce(text = lastSearch)

                binding.trackListView.visibility = if (binding.searchBar.hasFocus() && s?.isEmpty() == true) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        searchTextWatcher.let { binding.searchBar.addTextChangedListener(it) }

        binding.resetText.setOnClickListener {
            binding.searchBar.setText("")
            binding.resetText.visibility = View.GONE
            binding.searchBar.clearFocus()
            clearContent()
        }

        binding.searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.searchBar.text.isNotEmpty()) {
                    lastSearch = binding.searchBar.text.toString()
                    viewModel.searchDebounce(binding.searchBar.text.toString())
                }
            }
            false
        }

        binding.searchBar.setOnFocusChangeListener { _, hasFocus ->
            viewModel.getSearchHistory()
            binding.trackListView.visibility =
                if (hasFocus && binding.searchBar.text.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.resetText.setOnClickListener {
            binding.searchBar.setText(lastSearch)
            viewModel.searchDebounce(lastSearch)
        }

        binding.resetText.setOnClickListener {
            viewModel.clearHistory()
            binding.trackListView.visibility = View.GONE
        }
    }

    private fun clearContent() {
        trackAdapter.trackList.clear()
        trackAdapter.notifyDataSetChanged()
    }

    private fun render(state: TrackState) {
        when (state) {
            is TrackState.Content -> showContent(state.track)
            is TrackState.Empty -> showEmpty()
            is TrackState.Error -> showError()
            is TrackState.Loading -> showLoading()
        }
    }

    private fun showContent (trackList: List<Track>) {
        trackAdapter.trackList.clear()
        trackAdapter.trackList.addAll(trackList)
        trackAdapter.notifyDataSetChanged()

        binding.rvTrack.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.trackListView.visibility = View.GONE
    }

    private fun showLoading () {
        binding.progressBar.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.GONE
        binding.rvTrack.visibility = View.GONE
        binding.trackListView.visibility = View.GONE
    }

    private fun showError () {
        binding.placeholderImage.setImageResource(R.drawable.il_connection_error)
        binding.placeholderText.setText(R.string.not_connection)
        binding.placeholderMessage.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.rvTrack.visibility = View.GONE
        binding.trackListView.visibility = View.GONE

        binding.placeholderButton.visibility = View.VISIBLE
    }

    private fun showEmpty () {
        binding.placeholderImage.setImageResource(R.drawable.il_nothing_found)
        binding.placeholderText.setText(R.string.not_found)
        binding.placeholderButton.visibility = View.GONE
    }

    private fun showSearchHistory (trackList: ArrayList<Track>) {
        searchAdapter.trackList = trackList
        binding.rvTrack.adapter = searchAdapter
        searchAdapter.notifyDataSetChanged()

        binding.rvTrack.visibility = View.VISIBLE
        binding.rvHistory.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
        binding.trackListView.visibility = View.VISIBLE
    }

    private fun setAdapterListener(): TrackAdapter.Listener {
        return object : TrackAdapter.Listener {
            override fun onClick(track: Track) {
                viewModel.onClick(track = track)
                val intent = Intent (this@SearchActivity, AudioPlayerActivivty::class.java)
                intent.putExtra(TRACK, TrackMapper.map(track))
                startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchTextWatcher?.let { binding.searchBar.removeTextChangedListener(it) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SHARED_PREFERENCES, binding.searchBar.text.toString())

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.searchBar.setText(savedInstanceState.getString(SHARED_PREFERENCES))
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}