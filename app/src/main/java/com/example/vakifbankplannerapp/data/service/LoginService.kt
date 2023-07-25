package com.example.vakifbankplannerapp.data.service

import com.example.vakifbankplannerapp.data.model.Login
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    //https://localhost:5001/api/Login

    @GET("Login/")
    fun sendUserInfo(
        @Query("sicil") sicil : Int,
        @Query("password") password : String
    ): Login

    @POST("Login")
    fun getUserResponse(
        @Query("sicil") sicil : String,
        @Query("password") password : String
    ): Login
}