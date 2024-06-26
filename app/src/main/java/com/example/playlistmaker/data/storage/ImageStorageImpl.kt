package com.example.playlistmaker.data.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.example.playlistmaker.data.ImageStorage
import java.io.File
import java.io.FileOutputStream
import java.util.Date

class ImageStorageImpl(private val context: Context) : ImageStorage {

    companion object {
        private const val PICTURE_QUALITY = 30
    }

    override fun saveImageToPrivateStorage(uri: String): String {
        val filePath =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "playlist_maker")
        if (!filePath.exists()) {
            filePath.mkdir()
        }

        val fileName = Date().time.toString() + "jpg"
        val file = File(filePath, fileName)
        val inputStream = context.contentResolver.openInputStream(uri.toUri())
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, PICTURE_QUALITY, outputStream)

        return file.toUri().toString()
    }

}