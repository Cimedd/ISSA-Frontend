package com.example.myapplication.util

import java.text.NumberFormat
import java.util.Locale

object Utility {
    fun moneyFormat(amount : Int) : String{
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getNumberInstance(localeID)
        return format.format(amount)
    }
}