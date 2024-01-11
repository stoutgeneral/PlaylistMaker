package com.example.playlistmaker

import com.example.playlistmaker.domain.models.Track

class ITunesResponse(val resultCount: Int, val results: List<Track>) {
}