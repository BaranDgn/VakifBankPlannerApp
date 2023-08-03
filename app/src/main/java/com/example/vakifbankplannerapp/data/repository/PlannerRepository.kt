package com.example.vakifbankplannerapp.data.repository

import com.example.vakifbankplannerapp.data.model.*
import com.example.vakifbankplannerapp.data.service.PlannerService
import com.example.vakifbankplannerapp.domain.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PlannerRepository @Inject constructor(
    private var api : PlannerService
) {
    //Login Checking
    suspend fun loginCheck(login: Login) : Resource<LoginResponse>{
        val responseForLogin = try{
            api.login(login)
        }catch(e: java.lang.Exception){
            return Resource.Error("Cannot get response")
        }
        return Resource.Success(responseForLogin)
    }

    // Fetch Meeting
    suspend fun getMeeting() : Resource<MeetingItem> {
        val response = try{
            api.getMeetings()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    // Search Meeting
    suspend fun searchMeeting(meetingName: SearchMeeting) : Resource<MeetingItem> {
        val response = try{
            api.searchMeeting(meetingName)
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    //Fetch Previous Meetings
    suspend fun getPastMeeting() : Resource<MeetingItem> {
        val response = try{
            api.getPastMeetings()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    //Add Meeting
    suspend fun addMeetingRepo(addMeeting : AddMeetingItem){
        try {
            api.addMeeting(addMeeting)
        }catch (e: java.lang.Exception){
            println("Data cannot fetch")
        }
    }
    //Delete Meeting
    suspend fun deleteMeetingRepo(deleteMeeting : DeleteItem){
        try {
            api.deleteMeeting(deleteMeeting)
        }catch (e: java.lang.Exception){
            println("Data cannot fetch")
        }
    }

    suspend fun updateMeetingRepo(updateMeeting: UpdateMeeting){
        try{
            api.updateMeeting(updateMeeting)
        }catch (e : java.lang.Exception){
            println("Data cannot updated")
        }
    }

    //Fetch Event
    suspend fun getEvent() : Resource<Event> {
        val response = try{
            api.getEvents()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    suspend fun addEvent(addEventItem: AddEventItem){
        try {
            api.addEvent(addEventItem)
        }catch (e: java.lang.Exception){
            println(e.message)
        }
    }

    suspend fun updateEvent(updateEvent: UpdateEvent){
        try{
            api.updateEvent(updateEvent)
        }catch (e: java.lang.Exception){
            println(e.message)
        }
    }
    suspend fun deleteEvent(deleteEvent: DeleteEvent) {
        try {
            api.deleteEvent(deleteEvent)
        } catch (e: java.lang.Exception) {
            println(e.message)
        }
    }

    //Fetch Previous Events
    suspend fun getPastEvent() : Resource<Event> {
        val response = try{
            api.getPastEvents()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    // Search Event
    suspend fun searchEvent(eventName: SearchEvent) : Resource<Event> {
        val response = try{
            api.searchEvent(eventName)
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    //Fetch Birthday
    suspend fun getBirthday() : Resource<Birthday> {
        val response = try{
            api.getBirthday()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }

    //Fetch Incoming Birthday
    suspend fun getIncomingBirthday() : Resource<IncomingBirthday> {
        val response = try{
            api.getIncomingBirthday()
        }catch (e: java.lang.Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(response)
    }
}