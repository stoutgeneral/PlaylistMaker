package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.ThemeStatus
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {

    companion object {
        const val SHARED_PREFS = "playlist_maker"
        const val HISTORY_KEY = "HISTORY_KEY"
        const val SIZE_TRACK_HISTORY = 9
    }

    private var sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    private val localSharedPrefs = sharedPrefs
    private var tracks = ArrayList<Track>()

    override fun addTrackHistory(track: Track) {
        tracks = getFromHistory()
        if (tracks.size > SIZE_TRACK_HISTORY) {
            tracks.removeLast()
        } else {
            val duplicateTrack = tracks.find { it.trackId == track.trackId }
            if (duplicateTrack != null) tracks.remove(duplicateTrack)
        }

        tracks.add(0, track)
        saveHistoryTrack(tracks)
    }

    private fun saveHistoryTrack(tracks: ArrayList<Track>) {
        localSharedPrefs.edit()
            .putString(HISTORY_KEY, Gson().toJson(tracks))
            .apply()
    }

    override fun getFromHistory(): ArrayList<Track> {
        val stringTracks = localSharedPrefs.getString(HISTORY_KEY, null)

        if (stringTracks != null) {
            return Gson().fromJson(stringTracks, object :
                TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList()
    }

    override fun clearHistoryTrack() {
        localSharedPrefs.edit()
            .clear()
            .apply()
    }
}