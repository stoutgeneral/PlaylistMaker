package com.example.playlistmaker

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

class TrackHistoryAdapter() : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    var trackList = ArrayList<Track>()
    private var isClickAvailability = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(trackList[position])
        holder.itemView.setOnClickListener {

            if (clickDebounce()) {
                val audioIntent = Intent(it.context, AudioPlayer::class.java)
                audioIntent.putExtra("track", Gson().toJson(trackList[position]))
                it.context.startActivity(audioIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return trackList.size
    }

    private fun clickDebounce(): Boolean {
        val condition = isClickAvailability

        if (isClickAvailability) {
            isClickAvailability = false
            handler.postDelayed({ isClickAvailability = true }, CLICK_DEBOUNCE_DELAY)
        }
        return condition
    }
}