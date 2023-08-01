package com.example.vakifbankplannerapp.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.vakifbankplannerapp.presentation.pastEvent.PastEventScreen
import com.example.vakifbankplannerapp.presentation.pastMeeting.PastMeetingScreen
import com.example.vakifbankplannerapp.presentation.addEvent.NewEventScreen
import com.example.vakifbankplannerapp.presentation.addEvent.NewMeetingScreen
import com.example.vakifbankplannerapp.presentation.birthday.BirthdayScreen
import com.example.vakifbankplannerapp.presentation.bottomBar.BottomBarScreen
import com.example.vakifbankplannerapp.presentation.event.EventScreen
import com.example.vakifbankplannerapp.presentation.meeting.MeetingScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeNavGraph(navHostController: NavHostController){

    NavHost(
        navController = navHostController,
        route = Graph.HOME,
        startDestination = BottomBarScreen.Meeting.route
    ) {

        composable(route = BottomBarScreen.Meeting.route){
            MeetingScreen(navController= navHostController)
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

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.featureNavGraph(
    navHostController: NavHostController


){
    navigation(
        route = Graph.FEATURE,
        startDestination = FeatureScreens.NewMeetingScreen.route
    ){
        composable(route = FeatureScreens.NewMeetingScreen.route){
            NewMeetingScreen(navHostController)
        }

        composable(route = FeatureScreens.NewEventScreen.route){
            NewEventScreen(navHostController)
        }
        composable(route = FeatureScreens.PastMeetingScreen.route){
            PastMeetingScreen(navHostController)
        }
        composable(route = FeatureScreens.PastEventsScreen.route){
            PastEventScreen(navHostController)
        }
    }
}

sealed class FeatureScreens(val route: String){
    object NewMeetingScreen : FeatureScreens(route = "meeting_screen")
    object NewEventScreen : FeatureScreens(route = "event_screen")
    object BirthdayScreen : FeatureScreens(route = "birthday_screen")
    object PastMeetingScreen : FeatureScreens(route= "past_meeting_screen")
    object PastEventsScreen : FeatureScreens(route= "past_event_screen")
}


