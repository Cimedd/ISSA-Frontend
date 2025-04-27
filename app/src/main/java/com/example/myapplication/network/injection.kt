package com.example.myapplication.network

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking

object injection {

    fun providedRepo(context : Context) : ApiRepo{
        val pref = SettingPref.getInstance(context.dataStore)
        val user = runBlocking { pref.getToken().firstOrNull() }
        Log.d("Token", user.toString())
        val apiService = ApiConfig.getApiService(user.toString())
        return ApiRepo(apiService)
    }

    fun provideSetting(context : Context) : SettingRepo{
        val pref = SettingPref.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService("")
        return SettingRepo(apiService,pref)
    }
}