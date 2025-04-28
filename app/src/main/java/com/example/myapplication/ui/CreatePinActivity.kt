package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.dataclass.DataRegis
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.SettingVMF
import com.example.myapplication.ui.viewmodel.SettingViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject

class CreatePinActivity: AppCompatActivity() {
    private lateinit var btnLogin: TextView
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var btnFinish: Button
    private lateinit var contextView: ImageView

    private val settingVM by viewModels<SettingViewModel>{
        SettingVMF.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_pin)

        inputPassword = findViewById(R.id.txtPin)
        inputConfirmPassword = findViewById(R.id.txtConfirmPin)
        btnFinish = findViewById(R.id.btnFinish)
        btnLogin = findViewById(R.id.btnLogin)
        contextView = findViewById(R.id.view)

        val regis = intent.getParcelableExtra<DataRegis>("register")

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnFinish.setOnClickListener {
            val password = inputPassword.text.toString().trim()
            val confirmPassword = inputConfirmPassword.text.toString().trim()

            // check if password and confirm password is same
            if (password != confirmPassword) {
                inputConfirmPassword.error = "Password tidak sama"
                inputConfirmPassword.requestFocus()
            } else if(password.length < 6) {
                inputPassword.error = "Password minimal 8 karakter"
                inputPassword.requestFocus()
            } else {
                settingVM.register(regis?.email ?: "",
                    regis?.name ?:"",
                    regis?.password ?: "",
                    regis?.phoneNumber ?: "",
                    inputPassword.text.toString()
                    ).observe(this){
                    when(it){
                        is Result.Error -> {
                            Toast.makeText(this, it.error, Toast.LENGTH_SHORT).show()}
                        Result.Loading -> {}
                        is Result.Success ->{
                            val alertDialogBuilder = AlertDialog.Builder(this)
                            alertDialogBuilder.setTitle("Register Successful")
                            alertDialogBuilder.setMessage("Check your email to verify the account")
                            alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            val alertDialog = alertDialogBuilder.create()
                            alertDialog.show()
                        }
                    }
                }
            }
        }
    }

//    private fun register(password: String) {
//        // get data from intent
//        val bundle = intent.extras
//        val nama = bundle?.get("nama").toString()
//        val email = bundle?.get("email").toString()
//        val nohp = bundle?.get("nohp").toString()
//
//        // send data to server
//        AndroidNetworking.post("https://dompetku-api.vercel.app/api/auth/register")
//            .setTag("register")
//            .setPriority(Priority.MEDIUM)
//            .addBodyParameter("name", nama)
//            .addBodyParameter("email", email)
//            .addBodyParameter("password", password)
//            .addBodyParameter("nohp", nohp)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response", response.toString())
//                    if(response.getString("success").equals("true")) {
//                        MaterialAlertDialogBuilder(this@CreatePinActivity)
//                            .setTitle("Registrasi Berhasil")
//                            .setMessage("Silahkan login menggunakan akun anda untuk melanjutkan")
//                            .setPositiveButton("Login") { dialog, which ->
//                                val intent = Intent(this@CreatePinActivity, LoginActivity::class.java)
//                                startActivity(intent)
//                            }
//                            .show()
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@CreatePinActivity)
//                        .setTitle("Gagal")
//                        .setMessage(jsonObject.getString("message"))
//                        .setPositiveButton("OK") { dialog, which ->
//                            dialog.dismiss()
//                        }
//                        .show()
//                }
//            })
//    }
}