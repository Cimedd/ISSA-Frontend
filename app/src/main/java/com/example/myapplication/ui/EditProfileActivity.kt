package com.example.myapplication.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.example.myapplication.R
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel
import com.example.myapplication.ui.viewmodel.SettingVMF
import com.example.myapplication.ui.viewmodel.SettingViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import org.json.JSONObject

class EditProfilActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var photoProfil : ImageView
    private lateinit var editName : EditText
    private lateinit var editNomor : EditText
    private lateinit var editEmail : EditText
    private lateinit var btnSimpan : Button
    private lateinit var radioGroup : RadioGroup
    private lateinit var idUser : String
    private lateinit var btnBack : ImageView
    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        sessionManager = SessionManager(this)
        photoProfil = findViewById(R.id.photoProfile)
        editName = findViewById(R.id.txtName)
        editNomor = findViewById(R.id.txtNoHp)
        editEmail = findViewById(R.id.txtEmail)
        btnSimpan = findViewById(R.id.btnSimpan)
        btnBack = findViewById(R.id.btnBack)

        btnBack.setOnClickListener {
            finish()
        }


        homeVM.getUser().observe(this){
            when(it){
                is Result.Error -> {}
                Result.Loading -> {}
                is Result.Success -> {
                    editName.setText(it.data.name)
                    editNomor.setText(it.data.phoneNumber)
                    editEmail.setText(it.data.email)
                }
            }
        }
        btnSimpan.setOnClickListener{
            val name = editName.text.toString().trim()
            val hp = editNomor.text.toString().trim()
            val email = editEmail.text.toString().trim()

            if (name.isEmpty()) {
                editName.error = "Name tidak boleh kosong"
                editName.requestFocus()
            } else if (hp.isEmpty()) {
                editNomor.error = "Nomor tidak boleh kosong"
                editNomor.requestFocus()
            }else if (email.isEmpty()) {
                editEmail.error = "Email tidak boleh kosong"
                editEmail.requestFocus()
            }else {
//                updateUser(name, hp, email)
            }

        }

//        getUser()
    }


//    private fun getUser() {
//        val token = sessionManager.getToken()
//
//        AndroidNetworking.get("https://dompetku-api.vercel.app/api/user/getprofile")
//            .setTag("profile")
//            .addHeaders("Authorization", "Bearer $token")
//            .setPriority(Priority.LOW)
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response", response.toString())
//
//                    val getJsonObject: JSONObject = response.getJSONObject("data")
//                    val user = getJsonObject.getJSONObject("user")
//
//                    if(response.getString("success").equals("true")) {
//                        setProfile(user)
//                        idUser = user.getString("_id")
//                    }
//                }
//
//                override fun onError(error: ANError) {
//                    Log.d("error", error.toString())
//                }
//            })
//    }
//
//    private fun setProfile(user: JSONObject) {
//        editName.setText(user.getString("name"))
//        editNomor.setText(user.getString("nohp"))
//        editEmail.setText(user.getString("email"))
//
//        // load image
//        Picasso.get()
//            .load(user.getString("image"))
//            .into(photoProfil)
//    }
//
//    private fun updateUser(name: String, nohp: String, email: String) {
//        sessionManager = SessionManager(this)
//        val token = sessionManager.getToken()
//
//        Log.d("id user", idUser)
//
//        AndroidNetworking.put("https://dompetku-api.vercel.app/api/user/$idUser")
//            .setTag("update")
//            .setPriority(Priority.MEDIUM)
//            .addBodyParameter("name", name)
//            .addBodyParameter("nohp", nohp)
//            .addBodyParameter("email", email)
//            .addHeaders("Authorization", "Bearer $token")
//            .build()
//            .getAsJSONObject(object : JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject) {
//                    Log.d("response Update User", response.toString())
//                    val data = response.getJSONObject("data")
//                    if(response.getString("success").equals("true")) {
//                        MaterialAlertDialogBuilder(this@EditProfilActivity)
//                            .setTitle("Berhasil")
//                            .setMessage("Update data berhasil!")
//                            .setPositiveButton("OK") { dialog, which ->
//                                dialog.dismiss()
//                                finish()
//                            }
//                            .show()
//                    }
//
//                }
//
//                override fun onError(error: ANError) {
//                    val error = error.errorBody
//                    val jsonObject = JSONObject(error)
//
//                    MaterialAlertDialogBuilder(this@EditProfilActivity)
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