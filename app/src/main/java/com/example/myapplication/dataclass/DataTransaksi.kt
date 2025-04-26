package com.example.myapplication.dataclass

import com.google.gson.annotations.SerializedName


data class DataTransaction(
    @field:SerializedName("created_At")
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
    val userID: Int? = null,

    @field:SerializedName("details")
    val detail: String
)

data class TransactionDetail(
    val senderId: String? = null,         // For user transfers
    @SerializedName("receiverId")
    val receiverId: String? = null,       // For user transfers
    @SerializedName("bankId")
    val bankId: String? = null,           // For bank-based transactions
    @SerializedName("accountNumber")
    val accountNumber: String? = null,    // For withdrawalsW
    @SerializedName("productId")
    val productId: String? = null,        // For top-up or voucher
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,      // For isi pulsa / phone-based services
    @SerializedName("billType")
    val billType: String? = null,         // For bill payment type (e.g., PLN, PDAM)
    @SerializedName("billID")
    val billID: String? = null,           // User ID for the bill system
    @SerializedName("referenceCode")
    val referenceCode: String? = null,    // Invoice or payment proof
    @SerializedName("note")
    val note: String? = null
)
