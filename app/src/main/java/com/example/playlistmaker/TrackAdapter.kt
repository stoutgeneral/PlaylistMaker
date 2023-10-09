package com.example.playlistmaker

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class TrackAdapter(sharedPreferences: SharedPreferences) : RecyclerView.Adapter<TrackViewHolder>() {

    var trackList = ArrayList<Track>()
    val searchHistory = SearchHistory(sharedPreferences)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.music_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        val track = trackList[position]

        holder.itemView.setOnClickListener {
            searchHistory.addTrackHistory(track)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}