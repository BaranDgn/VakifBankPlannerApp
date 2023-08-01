package com.example.vakifbankplannerapp.data.model

data class UpdateEvent(
    val id : Int,
    val eventName : String,
    val eventType : String,
    val eventDateTime : String,
    val eventNotes : String
)
