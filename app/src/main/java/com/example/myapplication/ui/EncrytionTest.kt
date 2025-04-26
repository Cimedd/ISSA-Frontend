package com.example.myapplication.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.example.myapplication.databinding.ActivityEncrytionTestBinding
import com.example.myapplication.util.KeyGenerator
import com.example.myapplication.util.SecurityUtil

class EncrytionTest : AppCompatActivity() {
    private  lateinit var binding: ActivityEncrytionTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEncrytionTestBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener{
            val text = binding.editTextText.text
            val encryption = SecurityUtil.encrypt(text.toString(), KeyGenerator.getSecretKey())
            binding.txtResult.text = encryption
        }

        binding.button2.setOnClickListener {
            val text = binding.txtResult.text
            val decrypt = SecurityUtil.decrypt(text.toString(), KeyGenerator.getSecretKey())
            binding.txtResult.text = decrypt
        }

        binding.button3.setOnClickListener {
            val text = binding.editTextText.text
            val encryption = SecurityUtil.sha256(text.toString())
            binding.txtResult.text = encryption
        }
    }
}