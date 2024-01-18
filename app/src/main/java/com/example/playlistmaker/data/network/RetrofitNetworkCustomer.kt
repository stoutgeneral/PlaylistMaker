package com.example.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

class RetrofitNetworkCustomer(private val context: Context) : NetworkClient {

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
        return false
    }

    override fun doRequest(dto: Any): Response {
        if (!isConnected()) {
            return Response().apply { resultCount = -1 }
        }
        if (dto !is TrackSearchRequest) {
            return Response().apply { resultCount = 400 }
        }

        val response = RetrofitCustomer.api.search(dto.expression).execute()
        val body = response.body() ?: Response()

        return body.apply { resultCount = response.code() }
    }
}