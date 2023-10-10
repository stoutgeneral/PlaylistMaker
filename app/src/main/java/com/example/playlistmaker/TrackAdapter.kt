package com.example.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(sharedPreferences: SharedPreferences) : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList = ArrayList<Track>()
    private val searchHistory = SearchHistory(sharedPreferences)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.music_view, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = trackList[position]
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            searchHistory.addTrackHistory(track)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}