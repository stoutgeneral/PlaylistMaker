package com.example.playlistmaker.presentation.search

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.SearchHistoryInteractor
import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchJob: Job? = null
    private val trackStateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = trackStateLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        searchJob?.cancel()
    }

    fun searchDebounce(changedText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            trackSearch(changedText)
        }
    }

    fun getTrackSearchHistory() {
        viewModelScope.launch {
            val history = searchHistoryInteractor.getHistoryTrack()
            historyLiveData.postValue(history)
        }
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun onClick(track: Track) {
        searchHistoryInteractor.addTrackHistory(track)
    }

    private fun renderState(state: TracksState) {
        trackStateLiveData.postValue(state)
    }

    private fun trackSearch(text: String) {
        if (text.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                trackInteractor.searchTracks(text).collect { pair ->
                    displaySearchResult(pair.first, pair.second)
                }
            }
        } else {
            renderState(TracksState.Default)
        }
    }

    private fun displaySearchResult(selectedTrack: List<Track>?, errorMessage: Int?) {
        when {
            selectedTrack.isNullOrEmpty() -> {
                renderState(TracksState.Empty)
            }

            errorMessage != null -> {
                renderState(TracksState.Error)
            }

            else -> {
                renderState(TracksState.Content(tracks = selectedTrack))
            }
        }
    }
}