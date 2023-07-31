package com.example.vakifbankplannerapp.data.model

data class UpdateMeeting(
    val id: Int,
    val teamName: String,
    val meetingName: String,
    val meetingDate: String,
    val meetingTime: String,
    val meetingContext: String,
    val meetingContent: String,
)
