package com.example.playlistmaker.ui.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class LibraryPlaylistAdapter (private val listener: Listener): RecyclerView.Adapter<LibraryPlaylistViewHolder> () {

    var playlists = mutableListOf<Playlist>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryPlaylistViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return LibraryPlaylistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: LibraryPlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)

        holder.itemView.setOnClickListener {
            listener.onClick(playlist)
        }
    }

    fun interface Listener {
        fun onClick(playlist: Playlist)
    }

}