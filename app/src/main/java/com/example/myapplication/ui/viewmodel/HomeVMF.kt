package com.example.myapplication.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.network.ApiRepo
import com.example.myapplication.network.SettingRepo
import com.example.myapplication.network.injection

class HomeVMF(private val repo : ApiRepo): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: HomeVMF? = null
        fun getInstance(context: Context): HomeVMF =
            instance ?: synchronized(this) {
                instance ?: HomeVMF(injection.providedRepo(context))
            }.also { instance = it }
    }
}