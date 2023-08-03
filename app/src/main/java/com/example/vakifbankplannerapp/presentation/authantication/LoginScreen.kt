package com.example.vakifbankplannerapp.presentation.authantication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.vakifbankplannerapp.R.drawable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Login
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.ui.theme.Shapes

@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    //Observing the loading from viewModel
    val authResource = loginViewModel.loginFlow.collectAsState()
    val isLoading by loginViewModel.loadingState.collectAsState()
    val context = LocalContext.current

    val passwordFocusRequester = FocusRequester()
    val focusManager : FocusManager = LocalFocusManager.current

    var passwordVisibility by remember { mutableStateOf(false) }

    val sicilNumberState = remember { mutableStateOf(TextFieldValue()) }
    val passwordState = remember { mutableStateOf(TextFieldValue()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffFFAE42)), //Color(0xffFBFCF8)
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = drawable.vector_passive),
                contentDescription = "login",
                modifier = Modifier
                    .size(150.dp)
                    .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    //.shadow(5.dp)
            )

            //Sicil Number
            TextField(
                value = sicilNumberState.value,
                onValueChange = {sicilNumberState.value = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(15.dp))
                    .focusRequester(focusRequester = FocusRequester()),
                leadingIcon = { Icon(imageVector = InputType.SicilNum.icon, contentDescription = null)},
                label = { Text(text = InputType.SicilNum.label) },
                //shape = Shapes.small,
                shape = Shapes.medium,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent),
                singleLine = true,
                keyboardOptions = InputType.SicilNum.keyboardOptions,
                visualTransformation = InputType.SicilNum.visualTransformation,
                keyboardActions = KeyboardActions(
                    onNext = {passwordFocusRequester.requestFocus()})
            )

            //Password
            TextField(
                value = passwordState.value,
                onValueChange = {passwordState.value = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(1.dp, RoundedCornerShape(15.dp))
                    .focusRequester(focusRequester = passwordFocusRequester),
                leadingIcon = { Icon(imageVector = InputType.Password.icon, contentDescription = null)},
                label = { Text(text = InputType.Password.label) },
                shape = Shapes.medium,
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent),
                singleLine = true,
                keyboardOptions = InputType.Password.keyboardOptions,
                visualTransformation = if(passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }),

                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(painter = painterResource(drawable.password_eye),
                            contentDescription = null,
                            tint = if(passwordVisibility) Color.DarkGray else Color.Gray)
                    }
                }
            )
            //Button
            Button(
                onClick = {
                    val sicilText = sicilNumberState.value.text.trim()
                    val passwordText = passwordState.value.text

                    if (sicilText.isEmpty() || passwordText.isEmpty()) {
                        Toast.makeText(context, "Lütfen, Sicil Numara ve Şifre değerleriniz Doldurunuz.", Toast.LENGTH_SHORT).show()
                    } else {
                        try {
                            val username = sicilText.toIntOrNull()
                            val password = passwordText

                            val login = username?.let { Login(it, password) }
                            if (login != null) {
                                loginViewModel.checkLogin(navController, context, login)
                            }
                        } catch (e: NumberFormatException) {
                            Toast.makeText(context, "Geçersi Değer! Lütfen geçerli bir değer giriniz.", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(15.dp)),
                colors = ButtonDefaults.buttonColors(
                    Color(0xFF4E4F50),
                    contentColor = Color(0xFFE2DED0)
                )
            ) {
                Text(text = "GİRİŞ YAP", modifier = Modifier.padding(vertical = 8.dp))
            }

           // Spacer(modifier = Modifier.height(64.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp).padding(top=80.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.DarkGray, modifier = Modifier.padding(top = 8.dp))
                }
            }


        }


        authResource.value?.let{
            when(it){
                is Resource.Error -> {
                    Toast.makeText(context,"Sicil Numarası veya Şifre hatalı olabilir. Lütfen Kontrol ediniz.", Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(color = MaterialTheme.colors.secondary)
                }
                is Resource.Success -> {
                    LaunchedEffect("",Unit){
                        Toast.makeText(context,"VakıfBank Planner Uygulamasına Hoşgeldiniz!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
}

sealed class InputType(
    val label: String,
    val icon: ImageVector,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
){
    object SicilNum : InputType(
        label = "Sicil Numarası",
        icon = Icons.Default.Person,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.None),
        visualTransformation = VisualTransformation.None
    )
    object Password : InputType(
        label = "Şifre",
        icon= Icons.Default.Lock,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Password),
        visualTransformation = PasswordVisualTransformation()
    )
}