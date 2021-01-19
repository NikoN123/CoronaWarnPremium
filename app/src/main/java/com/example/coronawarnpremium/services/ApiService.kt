package com.example.coronawarnpremium.services

import com.example.coronawarnpremium.rest.interfaces.ApiClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private const val baseUrl = "https://our-random-api-url.com"
class ApiService {
    object UserAdapter {
        var userClient: ApiClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)
    }
}