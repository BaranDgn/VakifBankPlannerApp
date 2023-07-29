package com.example.vakifbankplannerapp.presentation.authantication

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Login
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


    fun checkLogin(navController: NavController,context: Context, login : Login ){
        viewModelScope.launch {
            val result = repoLogin.loginCheck(login)

            when(result){
                is Resource.Success->{
                    val response = result.data
                    if (response != null) {
                        if(response.isSuccessfull){
                            navController.navigate(Graph.HOME){
                                popUpTo(AuthScreen.Login.route){inclusive = true}
                            }
                            Toast.makeText(context,"Welcome to VakıfBank Planner App", Toast.LENGTH_LONG).show()
                        }else{
                            Toast.makeText(context, "Please check your sicil number and password, then try again - ${response.message}", Toast.LENGTH_LONG).show()
                        }
                    }else{
                        Toast.makeText(context, "There is a error at server", Toast.LENGTH_LONG).show()

                    }
                }
                is Resource.Error->{
                    Toast.makeText(context, "Please check your sicil number and password, then try again ", Toast.LENGTH_LONG).show()
                }
                is Resource.Loading->{

                }
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