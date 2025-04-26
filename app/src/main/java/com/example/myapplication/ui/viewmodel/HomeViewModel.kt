package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dataclass.DataContact
import com.example.myapplication.network.ApiRepo
import com.example.myapplication.network.Result
import kotlinx.coroutines.launch


class HomeViewModel(private val repo: ApiRepo) : ViewModel() {

    private val _contact = MutableLiveData<Result<List<DataContact>>>()
    val contact : LiveData<Result<List<DataContact>>> = _contact

    fun getHistory()= liveData{
        emit(Result.Loading)
        try {
            val response = repo.getTransaction()
            if(response.status == "success"){
                emit(Result.Success(response.transactions))
            }
            else{
                emit(Result.Error( response.message ))
            }
        }
        catch (e : Exception){
            emit(Result.Error( e.message ?: "Unknown error"))
        }
    }

    fun doTransaction(type : String, status : String, amount : Int, receiverId : String? = null) = liveData {
        emit(Result.Loading)
        try {
            val response = repo.makeTransaction(type,status,amount, receiverId)
            if(response.status == "success"){
                emit(Result.Success(response.message))
            }
            else{
                emit(Result.Error( response.message ))
            }
        }
        catch (e : Exception){
            emit(Result.Error( e.message ?: "Unknown error"))
        }
    }

    fun getContact(){
        viewModelScope.launch {
            _contact.value = Result.Loading
            try {
                val response = repo.getUserContact()
                val contact = response.contacts ?: emptyList()
                if(response.status == "success"){
                    _contact.value = Result.Success(contact)
                }
            }
            catch (e : Exception){
                _contact.value = Result.Error( e.message ?: "Unknown error")
            }
        }
    }

    fun searchContact(phone : String){
        viewModelScope.launch {
            _contact.value = Result.Loading
            try {
                val response = repo.searchContact(phone)
                val contact = response.contacts
                if(response.status == "success"){
                    _contact.value = Result.Success(contact)
                }
                else{
                    _contact.value = Result.Success(emptyList())
                }
            }
            catch (e : Exception){
                _contact.value = Result.Error( e.message ?: "Unknown error")
            }
        }
    }

    fun getUser() = liveData {
        emit(Result.Loading)
        try {
            val response = repo.getUserData()
            if(response.status == "success"){
                emit(Result.Success(response.contacts))
            }
            else{
                emit(Result.Error( response.message ))
            }
        }
        catch (e : Exception){
            emit(Result.Error( e.message ?: "Unknown error"))
        }
    }
}