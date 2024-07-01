package com.example.playlistmaker.ui.audioplayer

import android.annotation.SuppressLint
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val playlistName: TextView = itemView.findViewById(R.id.playlist_add_name)
    private val cover: ImageView = itemView.findViewById(R.id.playlist_add_cover)
    private val playlistTrackCounter: TextView = itemView.findViewById(R.id.playlist_add_track_counter)

    @SuppressLint("CheckResult")
    fun bind(item: Playlist) {
        playlistName.text = item.name
        cover.contentDescription = item.name
        playlistTrackCounter.text = itemView.resources.getQuantityString(R.plurals.track_counter, item.tracks.size,item.tracks.size)

        Glide.with(itemView)
            .load(item.uri)
            .placeholder(R.drawable.placeholder)
            .transform(CenterCrop(), RoundedCorners(itemView.context.resources.getDimensionPixelOffset(R.dimen.offset_from_text)))
            .into(itemView.findViewById(R.id.playlist_add_cover))
    }
}