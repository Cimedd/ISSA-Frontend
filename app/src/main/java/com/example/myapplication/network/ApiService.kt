package com.example.myapplication.network

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    suspend fun register(@Body request: Map<String,String>): ResponseStatus

    @POST("login")
    suspend fun login(@Body request: Map<String,String>): ResponseLogin

    @POST("resend-verification")
    suspend fun sendVerification(@Part("email") email : RequestBody) : ResponseStatus

    @POST("logout")
    suspend fun logout() : ResponseStatus

    @GET("users")
    suspend fun getHomeData() : ResponseUser

    @GET("users/saldo")
    suspend fun getSaldo() : ResponseUser

    @GET("transactions")
    suspend fun getAllTransaction() : ResponseForTransaction

    @POST("transactions")
    suspend fun makeTransaction(@Body request: Map<String,String?>) : ResponseStatus

    @GET("providers/{id}")
    suspend fun getProvider(@Path("id") uid: Int) : ResponseProvider

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") uid: Int) : ResponseProduct

    @GET("contacts")
    suspend fun getContact() : ResponseContact

    @GET("contacts/{phone_number}")
    suspend fun searchContact(@Path("phone_number") phone : String) : ResponseContact


}