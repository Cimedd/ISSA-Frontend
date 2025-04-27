package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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


        btnBayar.setOnClickListener {
            val receiver = editReceiver.text.toString().trim()
            val encrypt = SecurityUtil.encryptTransaction(TransactionDetail(accountNumber = receiver, referenceCode =
            Utility.generateTransactionCode(), billType = "X"))
            if(receiver.isEmpty()) {
                editReceiver.error = "Penerima tidak boleh kosong"
                editReceiver.requestFocus()
                return@setOnClickListener
            } else {
                homeVM.doTransaction(type = TransactionType.TOP_UP.value, status = "success", amount = txtHarga.text.toString().toInt() , detail = encrypt ).observe(this
                ){
                    when(it){
                        is Result.Error -> TODO()
                        Result.Loading -> TODO()
                        is Result.Success -> {
                            val intent = Intent(this, BerhasilActivity::class.java)
                            intent.putExtra("title", "Topup Berhasil")
                            intent.putExtra("amount", txtHarga.text.toString().toInt())
                            startActivity(intent)
                        }
                    }
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