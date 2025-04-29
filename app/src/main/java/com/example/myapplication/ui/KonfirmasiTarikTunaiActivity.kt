package com.example.myapplication.ui

//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
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
import com.example.myapplication.R
import com.example.myapplication.dataclass.TransactionDetail
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.util.BiometricUtil
import com.example.myapplication.util.SecurityUtil
import com.example.myapplication.util.TransactionType
import com.example.myapplication.util.Utility
import com.squareup.picasso.Picasso
import java.text.DecimalFormat


class KonfirmasiTarikTunaiActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var btnBack: ImageView
    private lateinit var metodeIcon : ImageView
    private lateinit var metodeName : TextView
    private lateinit var txtRekening : TextView
    private lateinit var txtSubtotal : TextView
    private lateinit var txtTotal : TextView
    private lateinit var btnLanjut: Button
    private lateinit var cbBio : CheckBox
    private lateinit var txtPin : EditText

    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_konfirmasi_tarik_tunai)

        sessionManager = SessionManager(this)
        btnBack = findViewById(R.id.btnBack)

        metodeIcon = findViewById(R.id.metodeIcon)
        metodeName = findViewById(R.id.metodeName)
        txtRekening = findViewById(R.id.txtRekening)
        txtSubtotal = findViewById(R.id.txtSubtotal)
        txtTotal = findViewById(R.id.txtTotal)
        btnLanjut = findViewById(R.id.btnLanjut)
        cbBio = findViewById(R.id.cbBio)
        txtPin = findViewById(R.id.txtPin)

        val amount = "Rp " + intent.getStringExtra("amount")?.toInt()?.minus(2500)
            ?.let { Utility.moneyFormat(it.toInt()) }
        val rekening = intent.getStringExtra("rekening")
        val code = Utility.generateTransactionCode()

        val detail =SecurityUtil.encryptTransaction(TransactionDetail(accountNumber = rekening, bankId = "1", referenceCode = code ))
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

        homeVM.result.observe(this){
            val alertDialogBuilder = AlertDialog.Builder(this)
            when(it){
                is Result.Error -> {
                    alertDialogBuilder.setTitle("Withdraw Failed")
                    alertDialogBuilder.setMessage(it.error)
                    alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
                Result.Loading -> {}
                is Result.Success -> {
                    alertDialogBuilder.setTitle("Withdraw Successful")
                    alertDialogBuilder.setMessage("Money Successfully Withdrawed")
                    alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                        val intent = Intent(this, BerhasilActivity::class.java)
                        intent.putExtra("title", "Withdraw Berhasil")
                        intent.putExtra("amount", amount)
                        startActivity(intent)
                        finish()
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
                }
            }
        }

        btnLanjut.setOnClickListener {
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

    private fun setData() {
        val decimalFormat = DecimalFormat("#,###")

        // set metode pembayaran
        val metode = sessionManager.getMetode()
        metodeName.text = metode["name"]
        Picasso.get().load(metode["icon"]).into(metodeIcon)

        // set detail
        val amount = intent.getStringExtra("amount")
        val rekening = intent.getStringExtra("rekening")

        txtRekening.text = rekening
        txtSubtotal.text = "Rp " + decimalFormat.format(amount?.toInt()).toString()
        txtTotal.text = "Rp " + decimalFormat.format(amount?.toInt()?.minus(2500)).toString()
    }

//    private fun requestWithdraw(rekening: String, amount: String) {
//        val token = sessionManager.getToken()
//
//        AndroidNetworking.post("https://dompetku-api.vercel.app/api/transaction/withdraw")
//            .setTag("withdraw")
//            .setPriority(Priority.MEDIUM)
//            .addBodyParameter("amount", amount)
//            .addBodyParameter("rekening", rekening)
//            .addHeaders("Authorization", "Bearer $token")
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response", response.toString())
//                    if (response.getString("success").equals("true")) {
//
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@KonfirmasiTarikTunaiActivity)
//                        .setTitle("Gagal")
//                        .setMessage(jsonObject.getString("message"))
//                        .setPositiveButton("OK") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
//
//                    if(jsonObject.getString("code").equals("401")) {
//                        val intent = Intent(this@KonfirmasiTarikTunaiActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            })
//    }
}