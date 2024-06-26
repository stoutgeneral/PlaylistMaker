package com.example.playlistmaker.ui.search

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.util.ConvertTime
import java.text.SimpleDateFormat
import java.util.*

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    private val trackCover = itemView.findViewById<ImageView>(R.id.track_cover)

    fun bind(model: Track) {

        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = ConvertTime.convertToMinAndSec(model.trackTimeMillis)

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .transform(RoundedCorners(10))
            .into(trackCover)
    }
}