package com.example.myapplication.network

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody

class ApiRepo(private val api : ApiService) {

    suspend fun getUserContact(): ResponseContact{
     return api.getContact()
    }

    suspend fun getUserData() : ResponseUser{
        return api.getHomeData()
    }

    suspend fun searchContact(phone : String): ResponseContact{
        return api.searchContact(phone)
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

    suspend fun makeTransaction(type : String, status : String, amount : Int, receiverId : String? = null) : ResponseStatus{
        val body = mapOf("type" to type, "status" to status, "amount" to amount.toString(), "receiver_id" to receiverId)
         return api.makeTransaction(body)
    }
}