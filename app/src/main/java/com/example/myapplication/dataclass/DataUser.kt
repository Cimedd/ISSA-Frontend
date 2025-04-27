package com.example.myapplication.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

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
@Parcelize
data class Transaction(
    @field:SerializedName("type")
    val type : String,
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("amount")
    val amount: Int,
    @field:SerializedName("receiver_id")
    val recId: Int? = null
):Parcelable