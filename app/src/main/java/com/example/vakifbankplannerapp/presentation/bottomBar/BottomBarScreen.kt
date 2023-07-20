package com.example.vakifbankplannerapp.presentation.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Meeting : BottomBarScreen(
        route = "Meeting",
        title = "Meeting",
        icon = Icons.Default.Home
    )
    object Event : BottomBarScreen(
        route = "Event",
        title = "Event",
        icon = Icons.Default.Star
    )
    object Birthday : BottomBarScreen(
        route = "Birthday",
        title = "Birthday",
        icon = Icons.Default.Add
    )

}