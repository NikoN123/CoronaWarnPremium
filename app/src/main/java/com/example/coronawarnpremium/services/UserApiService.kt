package com.example.coronawarnpremium.services

import com.example.coronawarnpremium.rest.interfaces.UserClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
private const val baseUrl = "https://our-random-api-url.com"
class UserApiService {
    object UserAdapter {
        var userClient: UserClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserClient::class.java)
    }
}