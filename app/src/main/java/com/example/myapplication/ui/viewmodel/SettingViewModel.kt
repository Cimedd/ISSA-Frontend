package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.myapplication.network.Result
import com.example.myapplication.network.SettingRepo

class SettingViewModel(private val repo : SettingRepo): ViewModel() {

    fun login(username : String, password : String) = liveData{
        emit(Result.Loading)
        try {
            val response = repo.login(username,password)
            if(response.status == "success"){
                emit(Result.Success(response))
                repo.saveToDataStore(response.userId, response.token, response.name)
            }else {
                val errorMessage = response.message
                emit(Result.Error(errorMessage))
            }
        }
        catch (e : Exception){
            emit(Result.Error( e.message ?: "Unknown error"))
        }
    }

    fun register(email : String, username : String, password : String, phone : String) = liveData{
        emit(Result.Loading)
        try {
            val response = repo.register(email,username,password, phone)
            if(response.status == "success"){
                emit(Result.Success(response))
            }else {
                val errorMessage = response.message
                emit(Result.Error(errorMessage))
            }
        }
        catch (e : Exception){
            emit(Result.Error( e.message ?: "Unknown error"))
        }
    }

    suspend fun logout(){
        repo.logout()
    }

    suspend fun checkUser(): Boolean{
        return repo.checkUser()
    }

    suspend fun getUser() : String{
        return repo.getToken()
    }

    suspend fun getId() : String{
        return repo.getID()
    }

    suspend fun sendVerif(email : String){
        repo.sendVerif(email)
    }
}