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

    @POST("/Meeting/UpdateMeeting")
    suspend fun updateMeeting(@Body updateMeeting: UpdateMeeting)

    // Search Meeting
    @POST("/Meeting/SearchMeeting")
    suspend fun searchMeeting(@Body meetingName: SearchMeeting) : MeetingItem


    //Events
    @POST("api/Event/GetEvent")
    suspend fun getEvents() : Event

    @POST("/api/Event/PostEvent")
    suspend fun addEvent(@Body addEventItem: AddEventItem)
    //Delete Event
    @POST("/api/Event/DeleteEvent")
    suspend fun deleteEvent(@Body deleteEvent: DeleteEvent)

    //UpdateEvent
    @POST("/api/Event/UpdateEvent")
    suspend fun updateEvent(@Body updateEvent: UpdateEvent)

    //Past Events
    @POST("api/Event/GetPreviousEvents")
    suspend fun getPastEvents() : Event

    //Search Event
    @POST("api/Event/SearchEvent")
    suspend fun searchEvent(@Body eventName: SearchEvent) : Event

    //Incoming Birthdays
    @POST("BDay/getUpcomingBDays")
    suspend fun getIncomingBirthday() : IncomingBirthday



    //Birthdays
    @POST("BDay/GetAllBDay")
    suspend fun getBirthday() : Birthday
}