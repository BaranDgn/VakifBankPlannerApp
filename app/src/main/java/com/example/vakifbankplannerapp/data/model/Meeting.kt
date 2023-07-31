package com.example.vakifbankplannerapp.data.model


data class Meeting(
    val id: Int,
    val isMeeting: Boolean,
    val meetingContent: String,
    val meetingContext: String,
    val meetingDate: String,
    val meetingName: String,
    val meetingTime: String,
    val teamName: String
)
