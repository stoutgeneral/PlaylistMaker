package com.example.playlistmaker.presentation.search

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.SearchHistoryInteractor
import com.example.playlistmaker.domain.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.TracksState

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val handler = Handler(Looper.getMainLooper())

    private val trackStateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = trackStateLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { trackSearch(changedText) }


        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    fun getTrackSearchHistory() {
        historyLiveData.postValue(searchHistoryInteractor.getHistoryTrack())
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

            trackInteractor.searchTracks(text, object : TrackInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: Int?) {
                    when {
                        errorMessage != null -> {
                            renderState(TracksState.Error)
                        }
                        foundTracks.isNullOrEmpty() -> {
                            renderState(TracksState.Empty)
                        }
                        else -> {
                            renderState(
                                TracksState.Content(
                                    tracks = foundTracks
                                )
                            )
                        }
                    }
                }
            })
        }
    }
}