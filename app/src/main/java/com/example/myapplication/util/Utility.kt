package com.example.myapplication.util

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Utility {

    const val local = "10.0.2.2"
    const val networks = "190c-2001-448a-50e2-3b18-7d1f-d142-9aeb-d5f9.ngrok-free.app"
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

    fun capitalizeFirstLetter(input: String): String {
        return input.replaceFirstChar { it.uppercase() }
    }

    fun formatDateOnly(input: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")

        val date = parser.parse(input)

        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return formatter.format(date!!)
    }

    fun formatDateTime(input: String): String {
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        parser.timeZone = TimeZone.getTimeZone("UTC")

        val date = parser.parse(input)

        val formatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        return formatter.format(date!!)
    }
}