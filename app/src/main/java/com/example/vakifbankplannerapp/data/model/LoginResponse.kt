package com.example.vakifbankplannerapp.data.model

data class LoginResponse(
    val isManager : Boolean,
    val isSuccessfull : Boolean,
    val message :String,
    val isBirthday : Boolean
)
