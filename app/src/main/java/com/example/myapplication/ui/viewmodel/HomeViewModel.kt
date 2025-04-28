package com.example.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.dataclass.DataContact
import com.example.myapplication.dataclass.DataProduct
import com.example.myapplication.dataclass.DataProvider
import com.example.myapplication.dataclass.DataTransaction
import com.example.myapplication.network.ApiRepo
import com.example.myapplication.network.Result
import com.example.myapplication.network.TransactionsItem
import kotlinx.coroutines.launch


class HomeViewModel(private val repo: ApiRepo) : ViewModel() {

    private val _contact = MutableLiveData<Result<List<DataContact>>>()
    val contact : LiveData<Result<List<DataContact>>> = _contact

    private val _provider = MutableLiveData<Result<List<DataProvider>>>()
    val provider : LiveData<Result<List<DataProvider>>> = _provider

    private val _product = MutableLiveData<Result<List<DataProduct>>>()
    val product : LiveData<Result<List<DataProduct>>> = _product

    private val _transaction = MutableLiveData<Result<List<TransactionsItem?>?>>()
    val transaction : LiveData<Result<List<TransactionsItem?>?>> = _transaction

    private val _result = MutableLiveData<Result<String>>()
    val result : LiveData<Result<String>> = _result

    fun getHistory(){
        viewModelScope.launch {
            _transaction.value = Result.Loading
            try {
                val response = repo.getTransaction()
                if(response.status == "success"){
                    _transaction.value = Result.Success(response.transactions)
                }
                else{
                    _transaction.value = Result.Error( response.message ?: "" )
                }
            }
            catch (e : Exception){
                _transaction.value = Result.Error( e.message ?: "Unknown error")
            }
        }
    }

    fun getProduct(id : Int){
        viewModelScope.launch {
            _product.value = Result.Loading
            try {
                val response = repo.getProduct(id)
                if(response.status == "success"){
                    _product.value = Result.Success(response.products)
                }
                else{
                    _product.value = Result.Error( response.message )
                }
            }
            catch (e : Exception){
                _product.value = Result.Error( e.message ?: "Unknown error")
            }
        }
    }
    fun getProvider(id : Int){
        viewModelScope.launch {
            _provider.value = Result.Loading
            try {
                val response = repo.getProvider(id)
                if(response.status == "success"){
                    _provider.value = Result.Success(response.provider)
                }
                else{
                    _provider.value = Result.Error( response.message )
                }
            }
            catch (e : Exception){
                _provider.value = Result.Error( e.message ?: "Unknown error")
            }
        }
    }

    fun getSaldo() = liveData {
        emit(Result.Loading)
        try {
            val response = repo.getUserSaldo()
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

    fun doTransaction(type : String, status : String, amount : Int, receiverId : String? = null, detail : String, pin : String = "") {
        viewModelScope.launch {
            _result.value = Result.Loading
            try {
                val response = repo.makeTransaction(type,status,amount, receiverId, detail, pin)
                if(response.status == "success"){
                    _result.value = Result.Success(response.message)
                }
                else{
                    _result.value = Result.Error(response.message)
                }
            }
            catch (e : Exception){
                _result.value = Result.Error( e.message ?: "Unknown error")
            }
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
                Log.d("Data", response.contacts.toString())
            }
            else{
                emit(Result.Error( response.message ))
            }
        }
        catch (e : Exception){
            emit(Result.Error( e.message ?: "Unknown error"))
        }
    }

    suspend fun logout(){
        repo.logout()
    }


}