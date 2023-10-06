package com.example.playlistmaker

import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.util.*

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val trackName = itemView.findViewById<TextView>(R.id.track_name)
    private val artistName = itemView.findViewById<TextView>(R.id.artist_name)
    private val trackTime = itemView.findViewById<TextView>(R.id.track_time)
    private val trackCover = itemView.findViewById<ImageView>(R.id.track_cover)

    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackTime.text = model.trackTime /*SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime)*/

        Glide.with(itemView)
            .load(model.artworkUrl100)
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.placeholder)
            .centerCrop()
            .into(trackCover)
    }
}