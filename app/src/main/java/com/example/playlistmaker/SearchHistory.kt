package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(sharePrefs: SharedPreferences) {
    companion object {
        const val HISTORY_TRACK_KEY = "HISTORY_TRACK_KEY"
        const val HISTORY_SIZE = 10
    }

    private var tracks = ArrayList<Track>()
    private val sharedPreferences = sharePrefs

    fun getHistoryTrack(): ArrayList<Track> {
        val trackList = sharedPreferences.getString(HISTORY_TRACK_KEY, null)
        if (!trackList.isNullOrBlank()) {
            return Gson().fromJson(trackList, object : TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList()
    }

    fun addTrackHistory (track: Track) {
        tracks = getHistoryTrack()
        if (tracks.size >= HISTORY_SIZE) {
            tracks.removeLast()
        } else {
            if (tracks.contains(track)) tracks.remove(track)
        }

        tracks.add(0, track)
        saveHistoryTrack(tracks)
    }

    private fun saveHistoryTrack (tracks: ArrayList<Track>) {
        sharedPreferences.edit()
            .putString(HISTORY_TRACK_KEY, Gson().toJson(tracks))
            .apply()
    }

    fun clearHistoryTrack () {
        sharedPreferences.edit()
            .clear()
            .apply()
    }
}