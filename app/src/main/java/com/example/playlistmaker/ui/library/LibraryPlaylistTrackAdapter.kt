package com.example.playlistmaker.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track

class LibraryPlaylistTrackAdapter (private val listener: Listener): RecyclerView.Adapter<LibraryPlaylistTrackViewHolder> () {

    var tracks = ArrayList<Track>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LibraryPlaylistTrackViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.activity_track, parent, false)

        return LibraryPlaylistTrackViewHolder(view) // ???
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: LibraryPlaylistTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnLongClickListener { listener.onClickLong(track.trackId) }
        holder.itemView.setOnClickListener { listener.onClickShort(track = track) }
    }

    interface Listener {
        fun onClickLong (trackInt: Int): Boolean
        fun onClickShort (track: Track)
    }

}