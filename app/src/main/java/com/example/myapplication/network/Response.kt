package com.example.myapplication.network

import com.example.myapplication.dataclass.DataContact
import com.example.myapplication.dataclass.DataProduct
import com.example.myapplication.dataclass.DataProvider
import com.example.myapplication.dataclass.DataTransaction
import com.example.myapplication.dataclass.DataUser
import com.google.gson.annotations.SerializedName

data class ResponseStatus(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String
)

data class ResponseTransaction(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("transactions")
    val transactions: List<DataTransaction>
)

data class ResponseContact(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("contacts")
    val contacts: List<DataContact>
)

data class ResponseUser(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("contacts")
    val contacts: DataUser
)

data class ResponseProvider(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("provider")
    val provider: List<DataProvider>
)

data class ResponseProduct(
    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("products")
    val products: List<DataProduct>
)

data class ResponseLogin(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("message")
    val message: String,
)




