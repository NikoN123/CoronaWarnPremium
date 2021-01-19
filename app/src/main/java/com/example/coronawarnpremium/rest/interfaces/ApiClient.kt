package com.example.coronawarnpremium.rest.interfaces

import com.example.coronawarnpremium.classes.RegisterRequest
import com.example.coronawarnpremium.classes.User
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    @GET("/")
    suspend fun loginAsync(identity: String, password: String): Response<User>
    @GET("/")
    suspend fun loginWithGuidAsync(guid: String, password: String): Response<User>
    @GET("/")
    suspend fun infectAsync(): Response<Void>
    @GET("/")
    suspend fun deInfectAsync(): Response<Void>
    @POST("/")
    suspend fun registerAsync(@Body request: RegisterRequest): Response<User>
    @GET("/{userId}")
    suspend fun sendContactRequest(@Path("userId") guid: String): Response<Void>
}