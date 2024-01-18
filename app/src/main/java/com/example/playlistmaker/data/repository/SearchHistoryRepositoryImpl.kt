package com.example.playlistmaker.data.repository

import android.content.Context
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.repository.SearchHistoryRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistoryRepositoryImpl(context: Context): SearchHistoryRepository {
    companion object {
        const val SHARE_PREF = "SHARE_PLAYLIST_MAKER"
        const val HISTORY_TRACK_KEY = "HISTORY_TRACK_KEY"
        const val HISTORY_SIZE = 9
    }

    private var tracks = ArrayList<Track>()
    private val sharePrefs = context.getSharedPreferences(SHARE_PREF, Context.MODE_PRIVATE)
    private val localeShareFrefs = sharePrefs

    override fun getHistoryTrack(): ArrayList<Track> {
        val trackList = localeShareFrefs.getString(HISTORY_TRACK_KEY, null)
        if (trackList != null) {
            return Gson().fromJson(trackList, object : TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList()
    }

    override fun addTrackHistory(track: Track) {
        tracks = getHistoryTrack()
        if (tracks.size > HISTORY_SIZE) {
            tracks.removeLast()
        } else {
            val duplicateTrack = tracks.find { it.trackId == track.trackId }
            if (duplicateTrack != null) tracks.remove(duplicateTrack)
        }

        tracks.add(0, track)
        saveHistoryTrack(tracks)
    }

    private fun saveHistoryTrack(tracks: ArrayList<Track>) {
        localeShareFrefs.edit()
            .putString(HISTORY_TRACK_KEY, Gson().toJson(tracks))
            .apply()
    }

    override fun clearHistoryTrack() {
        localeShareFrefs.edit()
            .clear()
            .apply()
    }
}