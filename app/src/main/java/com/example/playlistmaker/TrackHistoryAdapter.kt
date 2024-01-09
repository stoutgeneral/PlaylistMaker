package com.example.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackHistoryAdapter (): RecyclerView.Adapter<TrackViewHolder>() {

    var trackList = ArrayList<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])

        holder.itemView.setOnClickListener {
            val audioIntent = Intent (it.context, AudioPlayer::class.java)
            audioIntent.putExtra("track", Gson().toJson(trackList[position]))
            it.context.startActivity(audioIntent)
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }
}