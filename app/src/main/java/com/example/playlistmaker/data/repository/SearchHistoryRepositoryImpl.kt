package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import com.example.playlistmaker.data.db.AppDataBase
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(
    private val localSharedPrefs: SharedPreferences,
    private val gson: Gson,
    private val appDataBase: AppDataBase
) : SearchHistoryRepository {

    companion object {
        const val HISTORY_KEY = "HISTORY_KEY"
        const val SIZE_TRACK_HISTORY = 9
    }

    private var tracks = ArrayList<Track>()

    override fun addTrackHistory(track: Track) {
        tracks = getHistoryTrack()
        if (tracks.size > SIZE_TRACK_HISTORY) {
            tracks.removeLast()
        } else {
            val duplicateTrack = tracks.find { it.trackId == track.trackId }
            if (duplicateTrack != null) tracks.remove(duplicateTrack)
        }
        tracks.add(0, track)
        saveHistoryTrack(tracks)
    }

    private fun setFavoritesTrack(tracks: ArrayList<Track>, indicators: List<Int>) {
        for (track in tracks) { // мог напутать
            track.isFavorite = track.trackId in indicators
        }
    }

    private fun saveHistoryTrack(tracks: ArrayList<Track>) {
        localSharedPrefs.edit()
            .putString(HISTORY_KEY, gson.toJson(tracks))
            .apply()
    }

    private fun getHistoryTrack(): ArrayList<Track> {
        val stringTracks = localSharedPrefs.getString(HISTORY_KEY, null)

        if (stringTracks != null) {
            return Gson().fromJson(stringTracks, object :
                TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList()
    }

    override suspend fun getFromHistory(): ArrayList<Track> {
        val history = getHistoryTrack()
        val favorites = appDataBase.getFavoriteDao().getId()
        if (favorites != null) {
            setFavoritesTrack(history, favorites)
        }
        return history
    }

    override fun clearHistoryTrack() {
        localSharedPrefs.edit()
            .clear()
            .apply()
    }
}