package com.example.myapplication.dataclass

import com.google.gson.annotations.SerializedName

data class DataContact(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name : String,
    @field:SerializedName("phone_number")
    val phoneNumber: String,
)

data class DataUser(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name : String,
    @field:SerializedName("phone_number")
    val phoneNumber: String,
    @field:SerializedName("saldo")
    val saldo: Int,
    @field:SerializedName("point")
    val point: Int,
    @field:SerializedName("email")
    val email: Int,
)