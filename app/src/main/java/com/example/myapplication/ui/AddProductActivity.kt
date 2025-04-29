package com.example.myapplication.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityAddProductBinding
import com.example.myapplication.network.Result
import com.example.myapplication.ui.viewmodel.HomeVMF
import com.example.myapplication.ui.viewmodel.HomeViewModel

class AddProductActivity : AppCompatActivity() {
    private val homeVM by viewModels<HomeViewModel>{
        HomeVMF.getInstance(this)
    }
    private lateinit var binding : ActivityAddProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnProduct3.setOnClickListener {
            homeVM.addProduct(binding.txtNamaProducts.text.toString(), binding.txtCatId2.text.toString().toInt()
            , binding.txtPrice.text.toString().toInt(), binding.txtDesc.text.toString()).observe(this){
                when(it){
                    is Result.Error -> {Toast.makeText(this, it.error, Toast.LENGTH_LONG).show()}
                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        Toast.makeText(this,"Product Added", Toast.LENGTH_LONG).show()
                        binding.txtNamaProducts.text.clear()
                        binding.txtCatId2.text.clear()
                        binding.txtPrice.text.clear()
                        binding.txtDesc.text.clear()
                    }
                }
            }
        }
    }
}