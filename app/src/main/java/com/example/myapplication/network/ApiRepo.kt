package com.example.myapplication.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ApiRepo(private val api : ApiService) {

    suspend fun getContact():ResponseContact{
        return  api.getContact()
    }

    suspend fun  searchContact(phone : String): ResponseContact{
        return api.searchContact(phone)
    }

    suspend fun sendVerif(email :String): ResponseStatus{
        val body = mapOf("email" to email)
        val bodys = email.toRequestBody("text/plain".toMediaType())
        return api.sendVerification(bodys)
    }

    suspend fun getProduct(id : Int) : ResponseProduct{
        return api.getProduct(id)
    }

    suspend fun  getProvider(id: Int): ResponseProvider{
        return api.getProvider(id)
    }

    suspend fun getTransaction() : ResponseTransaction{
        return api.getAllTransaction()
    }
}