package com.example.vakifbankplannerapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vakifbankplannerapp.presentation.authantication.LoginScreeen

fun NavGraphBuilder.authNavGraph(
    navController: NavHostController,
    startDestination : String
) {

    navigation(
        route = Graph.AUTHENTICATION,
        startDestination = AuthScreen.Login.route//startDestination
    ){
        composable(route = AuthScreen.Login.route){
            LoginScreeen(navController)
        }
    }
}

sealed class AuthScreen(val route: String){
    object Login: AuthScreen(route = "LOGIN")
    object Splash : AuthScreen(route = "splash")
}