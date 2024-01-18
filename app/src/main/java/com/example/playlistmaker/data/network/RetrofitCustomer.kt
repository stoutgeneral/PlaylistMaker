package com.example.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitCustomer {

    private const val ITUNES_BASE_URL = "https://itunes.apple.com"
    private val customer: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ITUNES_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val api: ITunesApi by lazy {
        customer.create(ITunesApi::class.java)
    }
}

