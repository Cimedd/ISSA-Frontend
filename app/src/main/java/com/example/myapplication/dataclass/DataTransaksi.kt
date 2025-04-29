package com.example.myapplication.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataTransaction(
    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("type")
    val type: String,

    @field:SerializedName("amount")
    val amount: Int,

    @field:SerializedName("status")
    val status: String ,

    @field:SerializedName("user_id")
    val userID: Int,

    @field:SerializedName("user_id")
    val recID: Int? = null,

    @field:SerializedName("details")
    val detail: String
):Parcelable

data class TransactionDetail(   
    @SerializedName("bankId")
    val bankId: String? = null,           // For bank-based transactions
    @SerializedName("accountNumber")
    val accountNumber: String? = null,    // For withdrawals
    @SerializedName("productId")
    val productId: String? = null,        // For top-up or voucher
    @SerializedName("referenceCode")
    val referenceCode: String? = null,    // Invoice or payment proof
    @SerializedName("note")
    val note: String? = null
)
