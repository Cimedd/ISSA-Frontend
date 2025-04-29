package com.example.myapplication.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddProviderBinding
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel

class AddProviderActivity : AppCompatActivity() {
    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }
    private  lateinit var binding : ActivityAddProviderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProviderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnProduct2.setOnClickListener {
            homeVM.addProvider(binding.txtNamaProvider.text.toString(), binding.txtCatId.text.toString()).observe(this){
                when(it){
                    is Result.Error -> {
                        Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()}
                    Result.Loading -> {}
                    is Result.Success -> {Toast.makeText(this, "Provider added", Toast.LENGTH_LONG).show()
                        binding.txtNamaProvider.text.clear()
                        binding.txtCatId    .text.clear()}
                }
            }
        }
    }
}