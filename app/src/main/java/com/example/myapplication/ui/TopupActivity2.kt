package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.ui.adapter.AdapterTopup2
import com.example.myapplication.dataclass.DataTopup
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.text.DecimalFormat

class TopupActivity2 : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var sessionManager: SessionManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var dataTopup : ArrayList<DataTopup>
    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topup2)

        recyclerView = findViewById(R.id.recyclerTopup)
        dataTopup = ArrayList<DataTopup>()
        sessionManager = SessionManager(this)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }

        val name = intent.getStringExtra("name")
        val  id = intent.getIntExtra("id",1)

        recyclerView.layoutManager = LinearLayoutManager(this)
        homeVM.getProduct(id)

        homeVM.product.observe(this){
            when(it){
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    recyclerView.adapter = AdapterTopup2(this, it.data)
                }
            }
        }


    }

}