package com.example.vakifbankplannerapp.data.model


import com.google.gson.annotations.SerializedName

data class IncomingBirthdayItem(
    @SerializedName("birthDay")
    val birthDay: String,
    @SerializedName("counterOfDay")
    val counterOfDay: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("surname")
    val surname: String
)