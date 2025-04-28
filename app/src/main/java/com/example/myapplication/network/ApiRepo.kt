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

    suspend fun getTransaction() : ResponseForTransaction{
        return api.getAllTransaction()
    }

    suspend fun updateUser(){

    }

    suspend fun makeTransaction(type : String, status : String, amount : Int, receiverId : String? = null, detail : String, pin : String) : ResponseStatus{
       var body : Map<String,String?> = emptyMap()
        if(pin != ""){
            body = mapOf("type" to type, "status" to status, "amount" to amount.toString(), "receiver_id" to receiverId, "details" to  detail)
        }
        else{
            body = mapOf("type" to type, "status" to status, "amount" to amount.toString(), "receiver_id" to receiverId, "details" to  detail, "pin" to pin)
        }
         return api.makeTransaction(body)
    }

    suspend fun getUserSaldo() : ResponseUser{
        return api.getSaldo()
    }

    suspend fun logout(){
        api.logout()
    }
}