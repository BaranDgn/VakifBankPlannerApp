package com.example.vakifbankplannerapp.presentation.authantication

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Login
import com.example.vakifbankplannerapp.data.model.LoginResponse
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.presentation.navigation.AuthScreen
import com.example.vakifbankplannerapp.presentation.navigation.Graph
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel@Inject constructor(
    private val repoLogin : PlannerRepository,
): ViewModel() {

    private val _loginFlow = MutableStateFlow<Resource<Login>?>(null)
    val loginFlow: StateFlow<Resource<Login>?> = _loginFlow

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    var isAdmin = mutableStateOf(true)


    fun isAdminCheck(isAdmin:Boolean) : Boolean{
        return isAdmin
    }


    fun checkLogin(navController: NavController, context: Context, login: Login) {
        viewModelScope.launch {
            _loadingState.value = true
            val result = repoLogin.loginCheck(login)

            when (result) {
                is Resource.Success -> {
                    val response = result.data
                    if (response != null) {
                        if (response.isSuccessfull) {
                            isAdmin.value = response.isManager // Set the isAdmin value here
                            //isAdminCheck(response.isManager)
                            navController.navigate(Graph.HOME) {
                                popUpTo(AuthScreen.Login.route) { inclusive = true }
                            }
                            Toast.makeText(context, "VakıfBank Planner App Hoşgeldiniz..", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Lütfen, Sicil Numaranızı ve Şifrenizi kontrol edin ve tekrar deneyin.", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(context, "There is a error at server", Toast.LENGTH_LONG).show()
                    }
                    _loadingState.value = false
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Lütfen, Sicil Numaranızı ve Şifrenizi kontrol edin ve tekrar deneyin.", Toast.LENGTH_LONG).show()
                    _loadingState.value = false
                }
                else -> Unit
            }
        }
    }

/*

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(sicilNo: Int, sifre: String, navController: NavController)  {
        // Replace "YOUR_LOGIN_API_URL" with your actual login API endpoint
        val loginUrl = "https://vkfbnkmeetingapp.azurewebsites.net/api/Login/Login"

        val requestBody = JSONObject().apply {
            put("sicilNo", sicilNo)
            put("sifre", sifre)
        }.toString()

        val client = OkHttpClient()
        val request = Request.Builder()
            .url(loginUrl)
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody))
            .build()

        GlobalScope.launch(Dispatchers.IO) {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        // Successful login
                        withContext(Dispatchers.Main) {
                            _loginResult.value = LoginResult.Success
                            navController.navigate(Graph.HOME){} //
                           // Toast.makeText(context, "VakıfBank Toplantı uygulamasına hoşgeldiniz...", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        // Unsuccessful login
                        withContext(Dispatchers.Main) {
                            _loginResult.value = LoginResult.Error("Login failed")
                        }
                    }
                }
            } catch (e: IOException) {
                // Handle network or IO errors
                withContext(Dispatchers.Main) {
                    _loginResult.value = LoginResult.Error("Login failed")
                }
            } catch (e: JSONException) {
                // Handle JSON parsing errors
                withContext(Dispatchers.Main) {
                    _loginResult.value = LoginResult.Error("Login failed")
                }
            }
        }
    }

 */

}

/*
sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val errorMessage: String) : LoginResult()
}

 */