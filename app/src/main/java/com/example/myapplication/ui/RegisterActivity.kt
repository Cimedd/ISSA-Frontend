package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.SettingVMF
import com.example.myapplication.ui.viewmodel.SettingViewModel
import com.example.myapplication.util.SecurityUtil
import com.example.myapplication.util.Utility

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnLogin: TextView
    private lateinit var inputNama: EditText
    private lateinit var inputEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputHp: EditText
    private lateinit var btnNext: Button
    private val settingVM by viewModels<SettingViewModel>{
        SettingVMF.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnNext = findViewById(R.id.btnNext)
        inputNama = findViewById(R.id.txtNama)
        inputEmail = findViewById(R.id.txtEmail)
        inputHp = findViewById(R.id.txtNoHp)
        btnLogin = findViewById(R.id.btnLogin)
        inputPassword = findViewById(R.id.txtPassword)

        btnLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btnNext.setOnClickListener {
            if(inputNama.text.toString().isEmpty()) {
                inputNama.error = "Nama tidak boleh kosong"
                inputNama.requestFocus()
            } else if(inputEmail.text.toString().isEmpty()) {
                inputEmail.error = "Email tidak boleh kosong"
                inputEmail.requestFocus()
            } else if(inputHp.text.toString().isEmpty()) {
                inputHp.error = "No HP tidak boleh kosong"
                inputHp.requestFocus()
            } else {
                val nama = inputNama.text.toString().trim()
                val email = inputEmail.text.toString().trim()
                val hp = inputHp.text.toString().trim()
                val pass = inputPassword.text.toString().trim()

                settingVM.register(nama,email,SecurityUtil.sha256(pass),hp).observe(this){
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

    private fun nextStep() {

    }
}