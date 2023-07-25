package com.example.vakifbankplannerapp.presentation.authantication

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vakifbankplannerapp.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel@Inject constructor(
    private val loginRepo : LoginRepository
): ViewModel() {

    val sicil = mutableStateOf(0)
    val password = mutableStateOf("")

    fun checkIfUserLogin(sicil : Int, password : String){
        viewModelScope.launch {
            loginRepo.sendLoginInfo(sicil, password)
        }
    }

}