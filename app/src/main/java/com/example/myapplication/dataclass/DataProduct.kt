package com.example.myapplication.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataProduct(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("description")
    val description: String,
    @field:SerializedName("price")
    val price: Int,
):Parcelable

data class  DataProvider(
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
)

