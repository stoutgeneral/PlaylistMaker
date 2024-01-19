package com.example.playlistmaker.ui.search

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class TrackAdapter(private var listener: Listener) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    var tracks = ArrayList<Track>()
    private var isClickAvailability = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                listener.onClick(track = track)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    private fun clickDebounce(): Boolean {
        val condition = isClickAvailability
        if (isClickAvailability) {
            isClickAvailability = false
            handler.postDelayed({ isClickAvailability = true }, CLICK_DEBOUNCE_DELAY)
        }
        return condition
    }

    interface Listener {
        fun onClick(track: Track)
    }

}





