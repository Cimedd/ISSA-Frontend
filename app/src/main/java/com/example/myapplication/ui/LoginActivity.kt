package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.SettingVMF
import com.example.myapplication.ui.viewmodel.SettingViewModel
import com.example.myapplication.util.SecurityUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var inputHp: EditText
    private lateinit var inputPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: TextView
    private lateinit var sessionManager: SessionManager
    private val settingVM by viewModels<SettingViewModel>{
        SettingVMF.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        lifecycleScope.launch {
            Log.d("Token", settingVM.getUser())
           if(settingVM.checkUser()){
               if(settingVM.getRole() == "user"){
                   moveToMain()
               }
               else{
                   val intent = Intent(this@LoginActivity, ActivityAdminMain::class.java)
                   startActivity(intent)
                   finish()
               }

           }
        }
        inputHp = findViewById(R.id.txtNoHp)
        inputPassword = findViewById(R.id.txtPin)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)

        btnRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val hp = inputHp.text.toString().trim()
            val password = inputPassword.text.toString().trim()

            if (hp.isEmpty()) {
                inputHp.error = "Nomor HP tidak boleh kosong"
                inputHp.requestFocus()
            } else if (password.isEmpty()) {
                inputPassword.error = "Password tidak boleh kosong"
                inputPassword.requestFocus()
            } else {
                settingVM.login(inputHp.text.toString(),inputPassword.text.toString() ).observe(this){
                    when(it){
                        is Result.Error -> {
                            val alertDialogBuilder = AlertDialog.Builder(this)
                            if (it.error == "Email not verified. Please check your inbox"){
                                alertDialogBuilder.setTitle("Register Successful")
                                alertDialogBuilder.setMessage("Check your email to verify the account")
                                alertDialogBuilder.setPositiveButton("OK") { _, _ ->
                                   lifecycleScope.launch {
                                       settingVM.sendVerif(inputHp.text.toString())
                                   }
                                }
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            }
                            else{
                                showToast(it.error)
                            }
                        }
                        Result.Loading -> {
                            
                        }
                        is Result.Success -> {
                           moveToMain()
                        }
                    }
                }
            }
        }
    }
    private fun showToast(text : String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun moveToMain(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}