package com.example.myapplication.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ResponseForTransaction(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("transactions")
	val transactions: List<TransactionsItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

@Parcelize
data class TransactionsItem(

	@field:SerializedName("amount")
	val amount: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("receiver_id")
	val receiverId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("details")
	val details: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("status")
	val status: String? = null
): Parcelable
