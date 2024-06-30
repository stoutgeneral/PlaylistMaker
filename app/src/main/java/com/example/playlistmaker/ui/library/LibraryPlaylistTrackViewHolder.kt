package com.example.playlistmaker.ui.library

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.ConvertTime

class LibraryPlaylistTrackViewHolder (item: View) : RecyclerView.ViewHolder(item) {
    private val trackName: TextView = item.findViewById(R.id.playlist_add_name)
    private val cover: ImageView = item.findViewById(R.id.track_cover)
    private val artistName: TextView = item.findViewById(R.id.artist_name)
    private val trackTime: TextView = item.findViewById(R.id.track_time)

    fun bind(item: Track) {
        trackName.text = item.trackName
        cover.contentDescription = item.artistName + " : " + item.trackName
        artistName.text = item.artistName
        trackTime.text = ConvertTime.convertToMinAndSec(item.trackTimeMillis)

        Glide
            .with(itemView)
            .load(item.getCoverArtworkPlaylist())
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(itemView.findViewById(R.id.track_cover))
    }
}