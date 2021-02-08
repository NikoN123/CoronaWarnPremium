package com.example.coronawarnpremium.services

import com.example.coronawarnpremium.rest.interfaces.ApiClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//private const val baseUrl = "http://10.0.2.2:5000"
private const val baseUrl = "http://localhost:5000"
class ApiService() {
    object UserAdapter {
        var token = ""
        var userClient: ApiClient = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiClient::class.java)

        fun setUserToken(token: String){
            this.token = token
        }

        var httpClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
            val newRequest: Request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            chain.proceed(newRequest)
        }).build()

        var tokenClient: ApiClient = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiClient::class.java)
    }
}