package com.example.vakifbankplannerapp.data.repository


import com.example.vakifbankplannerapp.data.model.Login
import com.example.vakifbankplannerapp.data.service.LoginService
import com.example.vakifbankplannerapp.domain.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


@ActivityScoped
class LoginRepository@Inject constructor(
    private var loginApi : LoginService
) {

    suspend fun sendLoginInfo(sicilNo: Int, password : String) : Resource<Login>{
        val response = try{
            loginApi.sendUserInfo(sicilNo, password)
        }catch (e: java.lang.Exception){
            return Resource.Error("")
        }
        return Resource.Success(response)
    }



}