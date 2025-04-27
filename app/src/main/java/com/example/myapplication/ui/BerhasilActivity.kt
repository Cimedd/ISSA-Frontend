package com.example.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.util.Utility
import java.text.DecimalFormat

class BerhasilActivity : AppCompatActivity() {
    private lateinit var txtTitle: TextView
    private lateinit var txtAmount: TextView
    private lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_berhasil)

        txtTitle = findViewById(R.id.txtTitle)
        txtAmount = findViewById(R.id.txtAmount)
        btnBack = findViewById(R.id.btnBack)

        val title = intent.getStringExtra("title").toString()
        val amount = intent.getStringExtra("amount").toString()
        val decimalFormat = DecimalFormat("#,###")

        txtTitle.text = title
        txtAmount.text = "Rp " + Utility.moneyFormat(amount.toInt())

        btnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}