package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.dataclass.DataProduct
import com.example.myapplication.dataclass.TransactionDetail
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.util.BiometricUtil
import com.example.myapplication.util.SecurityUtil
import com.example.myapplication.util.TransactionType
import com.example.myapplication.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import java.text.DecimalFormat

class PembayaranActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var txtSaldo: TextView
    private lateinit var txtNama: TextView
    private lateinit var txtHarga: TextView
    private lateinit var editReceiver: EditText
    private lateinit var btnBayar: Button
    private lateinit var sessionManager: SessionManager
    private lateinit var txtKet: TextView
    private lateinit var cbBio : CheckBox
    private lateinit var txtPin : EditText

    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pembayaran)

        btnBack = findViewById(R.id.btnBack)
        txtSaldo = findViewById(R.id.txtSaldo)
        editReceiver = findViewById(R.id.txtReceiver)
        btnBayar = findViewById(R.id.btnBayar)
        sessionManager = SessionManager(this)
        txtKet = findViewById(R.id.txtKet)
        txtHarga = findViewById(R.id.txtHargaProduk)
        txtNama = findViewById(R.id.txtNamaProduk)


        val product = intent.getParcelableExtra<DataProduct>("product")
        txtNama.text = product?.name
        txtHarga.text = product?.price.toString()

        btnBack.setOnClickListener {
            finish()
        }

        val detail = SecurityUtil.encryptTransaction(TransactionDetail(accountNumber = editReceiver.text.toString(), bankId = "1", referenceCode ="code" ))

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

        homeVM.result.observe(this){
            val alertDialogBuilder = AlertDialog.Builder(this)
            when(it){
                is Result.Error -> {
                    alertDialogBuilder.setTitle("TopUp Failed")
                    alertDialogBuilder.setMessage(it.error)
                    alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
                Result.Loading -> {}
                is Result.Success -> {
                    alertDialogBuilder.setTitle("TopUp Successful")
                    alertDialogBuilder.setMessage("Money Successfully Transfered")
                    alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, BerhasilActivity::class.java)
                        intent.putExtra("title", "TopUp Berhasil")
                        intent.putExtra("amount", txtHarga.text.toString())
                        startActivity(intent)
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }
        }



        btnBayar.setOnClickListener {
            val receiver = editReceiver.text.toString().trim()
            val encrypt = SecurityUtil.encryptTransaction(TransactionDetail(accountNumber = receiver, referenceCode =
            Utility.generateTransactionCode(), billType = "X"))
            if(receiver.isEmpty()) {
                editReceiver.error = "Penerima tidak boleh kosong"
                editReceiver.requestFocus()
                return@setOnClickListener
            } else {
                if(isFinger){
                    BiometricUtil.showBiometricPrompt(
                        activity = this,
                        onSuccess = {
                            homeVM.doTransaction(type = TransactionType.WITHDRAW.toString(),
                                status = "success", amount = txtHarga.text.toString().toInt(), detail = "detail" )
                        },
                        onError = {
                            Toast.makeText(this, "Failed to verify!", Toast.LENGTH_SHORT).show()
                        }
                    )
                }
                else{
                    homeVM.doTransaction(type = TransactionType.WITHDRAW.toString(),
                        status = "success", amount = txtHarga.text.toString().toInt(), detail = "detail", pin = txtPin.text.toString() )
                }
            }

        }
    }

    /*private fun requestTopup(code: String, receiver: String, context: Context) {
        val token = sessionManager.getToken()

        AndroidNetworking.post("https://dompetku-api.vercel.app/api/transaction/topup")
            .setTag("topup")
            .setPriority(Priority.MEDIUM)
            .addBodyParameter("receiver", receiver)
            .addBodyParameter("product_code", code)
            .addHeaders("Authorization", "Bearer $token")
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    try {
                        val data = response.getJSONObject("data")
                        Log.d("response sukses", response.toString())
                        if(response.getString("success").equals("true")) {

                        }
                    } catch (e: Exception) {
                        Log.d("response gagal", response.toString())
                    }

                }

                override fun onError(error: ANError) {
                    val error = error.errorBody
                    val jsonObject = JSONObject(error)

                    MaterialAlertDialogBuilder(this@PembayaranActivity)
                        .setTitle("Gagal")
                        .setMessage(jsonObject.getString("message"))
                        .setPositiveButton("OK") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()

                    if(jsonObject.getString("code").equals("401")) {
                        val intent = Intent(this@PembayaranActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            })
    }

    private fun getSaldo() {
        val token = sessionManager.getToken()

        AndroidNetworking.get("https://dompetku-api.vercel.app/api/user/getprofile")
            .addHeaders("Authorization", "Bearer $token")
            .setTag("profile")
            .setPriority(Priority.LOW)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Log.d("response", response.toString())

                    val getJsonObject: JSONObject = response.getJSONObject("data")
                    val user = getJsonObject.getJSONObject("user")

                    if(response.getString("success").equals("true")) {
                        val decimalFormat = DecimalFormat("#,###")
                        txtSaldo.text = decimalFormat.format(user.getInt("saldo")).toString()

                    }
                }

                override fun onError(error: ANError) {
                    Log.d("error", error.toString())
                }
            })
    }*/
}