package com.example.playlistmaker.presentation.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.TrackState

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

    private val trackInteractor by lazy { Creator.provideTrackInteractor(context = getApplication()) } // есть несоответствие
    private val searchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(context = getApplication()) }

    private val handler = Handler(Looper.getMainLooper())
    private val stateLiveData = MutableLiveData<TrackState>()
    private val historyLiveData = MutableLiveData<ArrayList<Track>>()

    fun observeState(): LiveData<TrackState> = stateLiveData
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    private fun trackSearch(expression: String) { // пока выглядит всрато, возможно не правильная реализация
        trackInteractor.searchTrack(expression) { foundTracks, errorMessage ->
            when {
                errorMessage != null -> renderState(TrackState.Error)
                foundTracks.isNullOrEmpty() -> renderState(TrackState.Empty)
                else -> renderState(TrackState.Content(track = foundTracks))
            }
        }
    }

    fun searchDebounce(text: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { trackSearch(text) }
        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    fun onClick(track: Track) {
        searchHistoryInteractor.addTrackHistory(track)
    }

    fun clearHistory () {
        searchHistoryInteractor.clearHistoryTrack()
    }

    private fun renderState(state: TrackState) {
        stateLiveData.postValue(state)
    }

    fun getSearchHistory() {
        historyLiveData.postValue(searchHistoryInteractor.getHistoryTrack())
    }
}