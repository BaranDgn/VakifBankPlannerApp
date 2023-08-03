package com.example.vakifbankplannerapp.presentation.bottomBar

import com.example.vakifbankplannerapp.R

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: Int
) {
    object Meeting : BottomBarScreen(
        route = "Meeting",
        title = "Toplantılar",
        icon = R.drawable.calendar
    )
    object Event : BottomBarScreen(
        route = "Event",
        title = "Etkinlikler",
        icon = R.drawable.event
    )
    object Birthday : BottomBarScreen(
        route = "Birthday",
        title = "Doğum Günü",
        icon = R.drawable.cake
    )

}