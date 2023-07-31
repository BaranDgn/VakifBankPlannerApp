package com.example.vakifbankplannerapp.data.repository

import android.util.Log
import com.example.vakifbankplannerapp.data.model.*
import com.example.vakifbankplannerapp.data.service.PlannerService
import com.example.vakifbankplannerapp.domain.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
}