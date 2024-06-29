package com.example.playlistmaker.presentation.audioplayer


import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.FavoriteInteractor
import com.example.playlistmaker.domain.PlaylistInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.State
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.audioplayer.StateAudioPlayer
import com.example.playlistmaker.domain.repository.AudioPlayerRepository
import com.example.playlistmaker.presentation.models.PlaylistState
import com.example.playlistmaker.presentation.models.PlaylistStateTrack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import java.util.Locale


class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerRepository,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val DELAY_MILLIS = 300L
    }

    private var timerJob: Job? = null

    private val playState = MutableLiveData<StateAudioPlayer>(StateAudioPlayer.Default())
    fun observePlayState(): LiveData<StateAudioPlayer> = playState

    private var isFavorite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavorite

    private val playlistState = MutableLiveData<PlaylistState>(PlaylistState.Empty)
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState

    private val addedToPlaylistState = MutableLiveData<PlaylistStateTrack>()
    fun observeAddedPlaylistState(): LiveData<PlaylistStateTrack> = addedToPlaylistState

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


    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playState.postValue(StateAudioPlayer.Paused(getCurrentPlayerPosition()))
        timerJob?.cancel()
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            val isTrackFavorite = favoriteInteractor.isTrackFavorite(track.trackId)

            if (isTrackFavorite) {
                favoriteInteractor.delete(track)
            } else {
                favoriteInteractor.add(track)
            }
            isFavorite.postValue(!isTrackFavorite)
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

    private fun addTrackToPlaylist (playlist: Playlist, trackId: Int) {
        playlist.tracks.add(trackId)
        playlist.tracksCounter += 1

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.update(playlist)
            }
        }
    }

    fun addTrackInPlaylist(playlist: Playlist, track: Track) {
        if (playlist.tracks.isEmpty()) {
            addTrackToPlaylist(playlist, track.trackId)
            addedToPlaylistState.postValue(PlaylistStateTrack.Added(playlist.name))
        } else {
            if (playlist.tracks.contains(track.trackId)) {
                addedToPlaylistState.postValue(PlaylistStateTrack.Respond(playlist.name))
            } else {
                addTrackToPlaylist(playlist, track.trackId)
                addedToPlaylistState.postValue(PlaylistStateTrack.Added(playlist.name))
            }
        }
    }

    fun getPlaylists () {
        viewModelScope.launch {
            playlistInteractor.getAll().collect() {
                if (it.isEmpty()) {
                    renderState(PlaylistState.Empty)
                } else {
                    renderState(PlaylistState.Content(it))
                }
            }
        }
    }

    private fun renderState (state: PlaylistState) {
        playlistState.postValue(state)
    }
}