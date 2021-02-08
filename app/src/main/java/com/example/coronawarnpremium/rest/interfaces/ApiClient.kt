package com.example.coronawarnpremium.rest.interfaces

import com.example.coronawarnpremium.classes.*
import com.example.coronawarnpremium.ui.account.USer
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {
    /** Users **/
    @GET("/api/1/User/Login?Mode=Email")
    suspend fun loginAsync(@Query("identity") identity: String, @Query("password") password: String): Response<RequestClass>
    @GET("/api/1/User/DeInfect?")
    suspend fun deInfectAsync(@Query("id") id: String): Response<Void>
    @POST("/api/1/User/Register")
    suspend fun registerAsync(@Body request: RegisterRequest): Response<RequestClass>
    @POST("/api/1/User/Infect/{id}")
    suspend fun infectWithId(@Path("id") id:String, @Body encounterList: List<Infected>): Response<Void>
    @PATCH("/api/1/User/{id}/partial")
    suspend fun patchUser(@Path("id") guid: String, @Body request: USer): Response<User>
    @GET("/api/1/User/GetInfectedIds")
    suspend fun getInfectedIds(): Response<Array<String>>



    /** Contacts **/
    @GET("/api/1/Contact/request/{requestingId}/{email}")
    suspend fun sendContactRequest(@Path("requestingId") guid: String, @Path("email") email: String): Response<Void>
    @GET("/api/1/Contact/{id}/accept/{requestingId}")
    suspend fun acceptContactRequest(@Path("id") id: String, @Path("requestingId") requestingId: Int): Response<Void>
    @GET("/api/1/Contact/{id}/decline/{requestingId}")
    suspend fun declineContactRequest(@Path("id") id: String, @Path("requestingId") requestingId: Int): Response<Void>
}