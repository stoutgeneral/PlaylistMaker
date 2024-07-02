package com.example.playlistmaker.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object ConvertTime {

    fun convertToMinAndSec (millis: Int?): String {
        val formatter = DateTimeFormatter.ofPattern("mm:ss", Locale.getDefault())

        millis?.let {
            return formatter.format(LocalTime.ofSecondOfDay((it / 1000).toLong()))
        }?: run {
            return "00:00"
        }
    }

    fun convertToYear(year: String?): String? {
        year?.let {
            return LocalDate.parse(it).year.toString()
        }?: run {
            return ""
        }
    }

    fun convertToMin (millis: Int): String {
        return SimpleDateFormat("mm", Locale.getDefault()).format(millis)
    }
}