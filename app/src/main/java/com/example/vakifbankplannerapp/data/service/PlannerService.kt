package com.example.vakifbankplannerapp.data.service

import com.example.vakifbankplannerapp.data.model.*
import retrofit2.http.*

interface PlannerService {

    //Login
    @POST("api/Login/Login")
    suspend fun login(@Body login :Login) : LoginResponse

    //Meetings
    @POST("Meeting/GetMeeting")
    suspend fun getMeetings() : MeetingItem

    //Past Meetings
    @POST("/Meeting/GetPreviousMeetings")
    suspend fun getPastMeetings() : MeetingItem

    //Add Meeting
    @POST("/Meeting/AddMeeting")
    suspend fun addMeeting(@Body addMeetingItem: AddMeetingItem)

    //Delete Meeting
    @POST("/Meeting/DeleteMeeting")
    suspend fun deleteMeeting(@Body deleteMeetingItem : DeleteItem)

    //Events
    @POST("api/Event/GetEvent")
    suspend fun getEvents() : Event

    //Past Events
    @POST("api/Event/GetPreviousEvents")
    suspend fun getPastEvents() : Event

    //Birthdays
    @POST("BDay/GetAllBDay")
    suspend fun getBirthday() : Birthday

}