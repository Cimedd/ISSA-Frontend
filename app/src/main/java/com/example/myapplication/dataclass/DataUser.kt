package com.example.myapplication.dataclass

import com.google.gson.annotations.SerializedName

data class DataUser(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name : String,
    @field:SerializedName("phone_number")
    val phoneNumber: String,
)
