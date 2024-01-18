package com.example.playlistmaker.presentation.audioplayer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.models.StateAudioPlayer
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.presentation.models.TrackDetails
import com.example.playlistmaker.ui.audioplayer.AudioPlayerActivivty
import com.google.gson.Gson
import kotlinx.coroutines.Runnable

class AudioPlayerViewModel : ViewModel() {

    companion object {
        private const val DELAY_MILLIS = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private val audioPlayerInteractor: AudioPlayerRepository =
        Creator.provideAudioPlayerInteractor()

    private lateinit var playerRunnable: Runnable
    private val handler = Handler(Looper.getMainLooper())

    private val playState = MutableLiveData<Boolean>()
    private val playEnableState = MutableLiveData<Boolean>()
    private val secondState = MutableLiveData<Long>()
    private val track = MutableLiveData<TrackDetails>()

    fun observePlayState(): LiveData<Boolean> = playState
    fun observePlayButtonState(): LiveData<Boolean> = playEnableState
    fun observeSecondState(): LiveData<Long> = secondState

    override fun onCleared() {
        audioPlayerInteractor.stoppingPlayer()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun preparePlayer(url: String) {
        audioPlayerInteractor.preparePlayer(url = url) { state ->
            when (state) {
                StateAudioPlayer.PREPARED -> playEnableState.postValue(true)
                else -> {}
            }
        }
    }

    fun setTrackData(trackData: String) {
        val trackObj = Gson().fromJson(trackData, TrackDetails::class.java)
        track.value = trackObj
    }

    fun playbackControl(count: Long) {
        if (count > 0) {
            audioPlayerInteractor.changingPlayer { state ->
                when (state) {
                    StateAudioPlayer.PREPARED, StateAudioPlayer.PAUSED -> {
                        playState.postValue(true)
                        handler.removeCallbacks(playerRunnable)
                    }
                    StateAudioPlayer.PLAYING -> {
                        playState.postValue(true)
                        startTimer(count)
                    }
                    else -> {}
                }
            }
        }
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playState.postValue(false)
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun startTimer(duration: Long) {
        val startTime = System.currentTimeMillis()
        playerRunnable =
            createUpdateTimerTask(startTime, duration * DELAY_MILLIS)
        handler.post(playerRunnable)
    }

    fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                // Сколько прошло времени с момента запуска таймера
                val elapsedTime = System.currentTimeMillis() - startTime
                // Сколько осталось до конца
                val remainingTime = duration - elapsedTime

                if (remainingTime > 0) {
                    // Если всё ещё отсчитываем секунды —
                    // обновляем UI и снова планируем задачу
                    secondState.postValue(remainingTime / DELAY_MILLIS)
                    handler.postDelayed(this, DELAY_MILLIS)
                } else {
                    pausePlayer()
                }
            }
        }
    }
}

/*

 */