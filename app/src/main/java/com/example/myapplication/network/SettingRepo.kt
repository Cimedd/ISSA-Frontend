package com.example.myapplication.network

import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class SettingRepo(private val api: ApiService, private val settingPref: SettingPref) {

    suspend fun login(email : String, password : String): ResponseLogin{
        val body = mapOf("phone_number" to email, "password" to password)
        return api.login(body)
    }

    suspend fun register(name: String, email : String, password : String, phone : String, pin : String): ResponseStatus{
        val body = mapOf("name" to name, "email" to email, "password" to password, "phone_number" to phone, "pin" to pin)
        return api.register(body)
    }

    suspend fun logout(){
        settingPref.loggedOut()
    }

    suspend fun getRole() : String{
        return settingPref.getRole().firstOrNull() ?: ""
    }

    suspend fun getID() : String{
        return settingPref.getID().firstOrNull() ?: ""
    }

    suspend fun saveToDataStore(uid: String, name: String, url: String, role : String){
        settingPref.loggedIn(uid, name, url, role)
    }

    suspend fun checkUser(): Boolean {
        val token = settingPref.getToken().firstOrNull()
        return token != ""
    }

    suspend fun getToken(): String {
        return settingPref.getToken().firstOrNull() ?: ""
    }

    suspend fun sendVerif(email :String): ResponseStatus{
        val body = email.toRequestBody("text/plain".toMediaType())
        return api.sendVerification(body)
    }
}