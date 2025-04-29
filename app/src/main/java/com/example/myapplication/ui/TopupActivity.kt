package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.ui.adapter.AdapterTopup
import com.example.myapplication.dataclass.DataTopup
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONArray
import org.json.JSONObject

class TopupActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var txtCategory: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var sessionManager: SessionManager

    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup)

        recyclerView = findViewById(R.id.recyclerTopup)
        sessionManager = SessionManager(this)
        txtCategory = findViewById(R.id.txtCategory)

        val category = intent.getStringExtra("category")
        txtCategory.text = "Pilih " + category?.capitalize()

        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        when(category){
            "internet" -> homeVM.getProvider(1)
            "listrik" -> homeVM.getProvider(2)
            "game" -> homeVM.getProvider(3)
            "voucher" -> homeVM.getProvider(4)
            "emoney" -> homeVM.getProvider(5)
            "pulsa" -> homeVM.getProvider(6)
        }

        homeVM.provider.observe(this){
            when(it){
                is Result.Error -> {}
                Result.Loading -> { }
                is Result.Success -> {
                    recyclerView.adapter = AdapterTopup(this, it.data)
                }
            }
        }


    }

}