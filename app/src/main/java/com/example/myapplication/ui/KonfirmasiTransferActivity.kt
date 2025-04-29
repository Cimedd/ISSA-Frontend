package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.dataclass.Transaction
import com.example.myapplication.dataclass.TransactionDetail
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.util.BiometricUtil
import com.example.myapplication.util.SecurityUtil
import com.example.myapplication.util.TransactionType
import com.example.myapplication.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.DecimalFormat

class KonfirmasiTransferActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var btnBack: ImageView
    private lateinit var txtName: TextView
    private lateinit var txtPhone: TextView
    private lateinit var txtAmount: TextView
    private lateinit var txtCatatan: TextView
    private lateinit var txtSaldo: TextView
    private lateinit var photoProfile: ImageView
    private lateinit var btnSend: TextView
    private lateinit var cbBio : CheckBox
    private lateinit var txtPin : EditText

    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_transfer)

        sessionManager = SessionManager(this)
        btnBack = findViewById(R.id.btnBack)
        txtName = findViewById(R.id.txtName)
        txtPhone = findViewById(R.id.txtPhone)
        txtAmount = findViewById(R.id.txtAmount)
        txtCatatan = findViewById(R.id.txtCatatan)
        txtSaldo = findViewById(R.id.txtSaldo)
        btnSend = findViewById(R.id.btnSend)
        cbBio = findViewById(R.id.cbBio)
        txtPin = findViewById(R.id.txtPin)

        btnBack.setOnClickListener {
            finish()
        }

        var isFinger = false
        cbBio.setOnClickListener {
            if (cbBio.isChecked) {
                if(BiometricUtil.isBiometricAvailable(this)){
                    txtPin.isVisible = false
                    isFinger = true
                }
                else{
                    cbBio.isChecked = false
                    Toast.makeText(this, "No Biometric Available", Toast.LENGTH_SHORT).show()
                }
            } else {
                txtPin.isVisible = true
                isFinger = true
            }
        }

        homeVM.getSaldo().observe(this){
            when(it){
                is Result.Error -> {
                    txtSaldo.text = ""
                    btnSend.isEnabled = false
                }
                Result.Loading -> txtSaldo.text = "0"
                is Result.Success -> txtSaldo.text = "Rp " + Utility.moneyFormat(it.data.saldo)
            }
        }

        val transaction = intent.getParcelableExtra<Transaction>("transaction")

        val nohp = intent.getStringExtra("nohp").toString()
        val amount = intent.getStringExtra("amount").toString()
        val catatan = intent.getStringExtra("catatan").toString()

        txtPhone.text = nohp
        txtAmount.text = "Rp " + Utility.moneyFormat(amount.toInt())
        txtCatatan.text = catatan

        val detailObj = TransactionDetail(note = catatan)
        val detail = SecurityUtil.encryptTransaction(detailObj)

        homeVM.result.observe(this){
            val alertDialogBuilder = AlertDialog.Builder(this)
            when(it){
                is Result.Error -> {
                    alertDialogBuilder.setTitle("Transfer Failed")
                    alertDialogBuilder.setMessage(it.error)
                    alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
                Result.Loading -> {}
                is Result.Success -> {
                    alertDialogBuilder.setTitle("Transfer Successful")
                    alertDialogBuilder.setMessage("Money Successfully Transfered")
                    alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, BerhasilActivity::class.java)
                        intent.putExtra("title", "Transfer Berhasil")
                        intent.putExtra("amount", amount)
                        startActivity(intent)
                        finish()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }
        }

        btnSend.setOnClickListener {
            if(isFinger){
                BiometricUtil.showBiometricPrompt(
                    activity = this,
                    onSuccess = {
                        homeVM.doTransaction(type = TransactionType.WITHDRAW.toString(),
                            status = "success", amount = amount.toInt(), detail = detail )
                    },
                    onError = {
                        Toast.makeText(this, "Failed to verify!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            else{
                homeVM.doTransaction(type = TransactionType.WITHDRAW.toString(),
                    status = "success", amount = amount.toInt(), detail = detail, pin = txtPin.text.toString() )
            }
        }

    }

//    private fun getCurrentUser() {
//        val token = sessionManager.getToken()
//        val decimalFormat = DecimalFormat("#,###")
//
//        AndroidNetworking.get("https://dompetku-api.vercel.app/api/user/getprofile")
//            .addHeaders("Authorization", "Bearer $token")
//            .setTag("current")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response detail", response.toString())
//                    val data: JSONObject = response.getJSONObject("data")
//                    val user: JSONObject = data.getJSONObject("user")
//
//                    if(response.getString("success").equals("true")) {
//                        txtSaldo.text = "Rp " + decimalFormat.format(user.getString("saldo").toBigInteger()).toString()
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@KonfirmasiTransferActivity)
//                        .setTitle("Gagal")
//                        .setMessage(jsonObject.getString("message"))
//                        .setPositiveButton("OK") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
//
//                    if(jsonObject.getString("code").equals("401")) {
//                        val intent = Intent(this@KonfirmasiTransferActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            })
//    }
//
//    private fun getUser(phone: String) {
//        val token = sessionManager.getToken()
//
//        AndroidNetworking.get("https://dompetku-api.vercel.app/api/user/phone/${phone}")
//            .addHeaders("Authorization", "Bearer $token")
//            .setTag("receiver")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response detail", response.toString())
//                    val data: JSONObject = response.getJSONObject("data")
//
//                    if(response.getString("success").equals("true")) {
//                        Picasso.get()
////                            .load(data.getString("image"))
////                            .into(photoProfile)
//
//                        txtName.text = data.getString("name")
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@KonfirmasiTransferActivity)
//                        .setTitle("Gagal")
//                        .setMessage(jsonObject.getString("message"))
//                        .setPositiveButton("OK") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
//
//                    if(jsonObject.getString("code").equals("401")) {
//                        val intent = Intent(this@KonfirmasiTransferActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            })
//    }
//
//    private fun transfer(nohp: String, amount: String, catatan: String) {
//        val token = sessionManager.getToken()
//
//        AndroidNetworking.post("https://dompetku-api.vercel.app/api/transaction/transfer")
//            .setTag("transfer")
//            .setPriority(Priority.MEDIUM)
//            .addBodyParameter("receiver", nohp)
//            .addBodyParameter("amount", amount)
//            .addBodyParameter("catatan", catatan)
//            .addHeaders("Authorization", "Bearer $token")
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response", response.toString())
//                    if(response.getString("success").equals("true")) {
//                        val intent = Intent(this@KonfirmasiTransferActivity, BerhasilActivity::class.java)
//                        intent.putExtra("title", "Transfer Berhasil")
//                        intent.putExtra("amount", amount)
//                        startActivity(intent)
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@KonfirmasiTransferActivity)
//                        .setTitle("Gagal")
//                        .setMessage(jsonObject.getString("message"))
//                        .setPositiveButton("OK") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
//
//                    if(jsonObject.getString("code").equals("401")) {
//                        val intent = Intent(this@KonfirmasiTransferActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            })
//    }
}