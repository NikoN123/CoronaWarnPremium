package com.example.coronawarnpremium.rest.interfaces

import com.example.coronawarnpremium.classes.RegisterRequest
import com.example.coronawarnpremium.classes.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserClient {
    @GET("/")
    suspend fun loginAsync(identity: String, password: String): Response<User>
    @GET("/")
    suspend fun loginWithGuidAsync(guid: String, password: String): Response<User>
    @GET("/")
    suspend fun infectAsync()
    @GET("/")
    suspend fun deInfectAsync()
    @POST("/")
    suspend fun registerAsync(@Body request: RegisterRequest): Response<User>
}