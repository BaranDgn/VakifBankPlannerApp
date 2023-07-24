package com.example.vakifbankplannerapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vakifbankplannerapp.presentation.addEvent.NewEventScreen
import com.example.vakifbankplannerapp.presentation.addEvent.NewMeetingScreen
import com.example.vakifbankplannerapp.presentation.birthday.BirthdayScreen
import com.example.vakifbankplannerapp.presentation.bottomBar.BottomBarScreen
import com.example.vakifbankplannerapp.presentation.event.EventScreen
import com.example.vakifbankplannerapp.presentation.meeting.MeetingScreen


@Composable
fun HomeNavGraph(navHostController: NavHostController){

    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Meeting.route
    ) {

        composable(route = BottomBarScreen.Meeting.route){
            MeetingScreen(navHostController)
        }
        composable(route = BottomBarScreen.Event.route){
            EventScreen(navHostController)
        }
        composable(route = BottomBarScreen.Birthday.route){
            BirthdayScreen()
        }
        featureNavGraph(navHostController = navHostController)
        authNavGraph(navHostController, AuthScreen.Login.route)
    }

}

fun NavGraphBuilder.featureNavGraph(
    navHostController: NavHostController


){
    navigation(
        route = Graph.FEATURE,
        startDestination = FeatureScreens.NewMeetingScreen.route
    ){
        //composable()
        composable(route = FeatureScreens.NewMeetingScreen.route){
            NewMeetingScreen(navHostController)
        }
        composable(route = FeatureScreens.NewEventScreen.route){
            NewEventScreen(navHostController)
        }
    }
}

sealed class FeatureScreens(val route: String){
    object NewMeetingScreen : FeatureScreens(route = "meeting_screen")
    object NewEventScreen : FeatureScreens(route = "event_screen")
    object BirthdayScreen : FeatureScreens(route = "birthday_screen")
}


