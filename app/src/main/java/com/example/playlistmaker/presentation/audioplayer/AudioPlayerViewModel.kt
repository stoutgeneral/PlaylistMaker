package com.example.playlistmaker.presentation.audioplayer


import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.FavoriteInteractor
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioplayer.StateAudioPlayer
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale


class AudioPlayerViewModel(private val audioPlayerInteractor: AudioPlayerRepository, private val favoriteInteractor: FavoriteInteractor) : ViewModel() {

    companion object {
        private const val DELAY_MILLIS = 300L
    }

    private var timerJob: Job? = null

    private val playState = MutableLiveData<StateAudioPlayer>(StateAudioPlayer.Default())
    fun observePlayState(): LiveData<StateAudioPlayer> = playState

    private val isFavorite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavorite

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        stoppingPlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    private fun stoppingPlayer() {
        audioPlayerInteractor.stoppingPlayer()
        StateAudioPlayer.Default()
    }

    fun playbackControl() {
        if (audioPlayerInteractor.getCurrentState() == State.PLAYING) {
            pausePlayer()
        } else {
            startPlayer()
        }
    }

    fun preparePlayer(url: String) {
        audioPlayerInteractor.preparePlayer(url = url) {
            playState.postValue(StateAudioPlayer.Prepared())
            timerJob?.cancel()
        }
        playState.postValue(StateAudioPlayer.Prepared())
    }


    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playState.postValue(StateAudioPlayer.Paused(getCurrentPlayerPosition()))
        timerJob?.cancel()
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.delete(track)
            } else {
                favoriteInteractor.add(track)
            }
            isFavorite.postValue(!track.isFavorite)
        }
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        playState.postValue(StateAudioPlayer.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun getCurrentPlayerPosition(): String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(audioPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.getCurrentState() == State.PLAYING) {
                delay(DELAY_MILLIS)
                playState.postValue(StateAudioPlayer.Playing(getCurrentPlayerPosition()))
            }
        }
    }
}