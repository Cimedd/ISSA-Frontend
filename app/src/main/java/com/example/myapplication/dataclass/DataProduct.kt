package com.example.myapplication.dataclass

import com.google.gson.annotations.SerializedName

data class DataProduct(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("price")
    val price: Int,
)

data class  DataProvider(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
)

