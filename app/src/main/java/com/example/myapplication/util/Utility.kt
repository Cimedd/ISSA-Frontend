package com.example.myapplication.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utility {

    const val local = "10.0.2.2"
    const val networks = "1e56-2001-448a-50e2-3b18-7d1f-d142-9aeb-d5f9.ngrok-free.app"
    fun moneyFormat(amount : Int) : String{
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getNumberInstance(localeID)
        return format.format(amount)
    }

    fun generateTransactionCode(): String {

        val datePart = SimpleDateFormat("yyMMdd").format(Date())

        // Step 2: Generate a random 4-digit number (for uniqueness)
        val randomNumber = (1000..9999).random() // Generates a random number between 1000 and 9999


        return "TXN-$datePart-$randomNumber"
    }
}