package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.dataclass.DataTransaction
import com.example.myapplication.network.TransactionsItem
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.util.SecurityUtil
import com.example.myapplication.util.Utility
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import org.json.JSONObject
import java.text.DecimalFormat

class DetailTopupActivity : AppCompatActivity() {
    private lateinit var btnBack: ImageView
    private lateinit var txtAmount: TextView
    private lateinit var txtStatus: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtId: TextView
    private lateinit var txtProduct: TextView
    private lateinit var sessionManager: SessionManager
    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_topup)

        btnBack = findViewById(R.id.btnBack)
        txtAmount = findViewById(R.id.txtAmount)
        txtStatus = findViewById(R.id.txtStatus)
        txtDate = findViewById(R.id.txtDate)
        txtId = findViewById(R.id.txtId)
        txtProduct = findViewById(R.id.txtProduct)
        sessionManager = SessionManager(this)

        btnBack.setOnClickListener {
            finish()
        }

        val transaction = intent.getParcelableExtra<TransactionsItem>("transaction")
        val detail = SecurityUtil.decryptTransaction(transaction?.details ?: "")

        txtAmount.text = transaction?.amount.toString()
        txtProduct.text = detail.referenceCode
        txtDate.text  = Utility.formatDateTime(transaction?.createdAt ?: "")
        txtStatus.text = transaction?.status

    }

//    private fun requestDetailTopup(context: Context) {
//        val id = intent.getStringExtra("id")
//        val token = sessionManager.getToken()
//        val decimalFormat = DecimalFormat("#,###")
//
//        AndroidNetworking.get("https://dompetku-api.vercel.app/api/transaction/${id}")
//            .setTag("detail withdraw")
//            .setPriority(Priority.LOW)
//            .addHeaders("Authorization", "Bearer $token")
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response topup", response.toString())
//                    val data = response.getJSONObject("data")
//                    val receiver = data.getJSONObject("receiver")
//                    val date = data.getString("createdAt")
//
//                    if(response.getString("success").equals("true")) {
//                        // format date to dd-mm-yyyy HH:mm
//                        val formatedDate = date.substring(8,10) + "-" + date.substring(5,7) + "-" + date.substring(0,4) + " " + date.substring(11,16)
//
//                        txtId.text = data.getString("reference")
//                        txtProduct.text = data.getString("product_code")
//                        txtAmount.text = "Rp " + decimalFormat.format(data.getInt("amount")).toString()
//                        txtStatus.text = data.getString("status")
//                        txtDate.text = formatedDate
//
//                        Picasso.get()
//                            .load(receiver.getString("image"))
//                            .into(photoProfile)
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@DetailTopupActivity)
//                        .setTitle("Gagal")
//                        .setMessage(jsonObject.getString("message"))
//                        .setPositiveButton("OK") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
//
//                    if(jsonObject.getString("code").equals("401")) {
//                        val intent = Intent(this@DetailTopupActivity, LoginActivity::class.java)
//                        startActivity(intent)
//                    }
//                }
//            })
//    }
}