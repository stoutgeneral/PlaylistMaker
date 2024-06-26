package com.example.playlistmaker.data

interface ImageStorage {
    fun saveImageToPrivateStorage (uri: String): String
}