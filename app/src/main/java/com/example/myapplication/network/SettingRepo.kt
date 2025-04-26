package com.example.myapplication.network

class SettingRepo(private val api: ApiService, private val settingPref: SettingPref) {

    suspend fun login(email : String, password : String){
        val body = mapOf("email" to email, "password" to password)
        api.login(body)
    }

    suspend fun register(name: String, email : String, password : String, phone : String){
        val body = mapOf("name" to name, "email" to email, "password" to password, "phone_number" to phone)
        api.register(body)
    }

    suspend fun logout(){

    }
}