package com.example.myapplication.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingPref private constructor(private val dataStore: DataStore<Preferences>) {
    private val userId = stringPreferencesKey("id")
    private val token = stringPreferencesKey("token")
    private val name = stringPreferencesKey(
        "name")

    fun getUser(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }

    suspend fun loggedIn(user : String, tokens: String, names : String){
        dataStore.edit { preferences  ->
            preferences[userId] = user
            preferences[token] = tokens
            preferences[name] = names
        }
    }

    suspend fun loggedOut(){
        dataStore.edit { preferences ->
            preferences[userId] = ""
            preferences[token] = ""
            preferences[name] = ""
        }
    }

    fun checkUser():Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[token] ?: ""
        }
    }


    fun getName():Flow<String>{
        return dataStore.data.map { preferences ->
            preferences[userId] ?: ""
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: SettingPref? = null
        fun getInstance(dataStore: DataStore<Preferences>): SettingPref {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}