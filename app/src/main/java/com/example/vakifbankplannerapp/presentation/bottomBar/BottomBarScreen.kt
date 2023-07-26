package com.example.vakifbankplannerapp.presentation.bottomBar

import com.example.vakifbankplannerapp.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Meeting : BottomBarScreen(
        route = "Meeting",
        title = "Meeting",
        icon = R.drawable.calendar
    )
    object Event : BottomBarScreen(
        route = "Event",
        title = "Event",
        icon = R.drawable.event
    )
    object Birthday : BottomBarScreen(
        route = "Birthday",
        title = "Birthday",
        icon = R.drawable.cake
    )

}