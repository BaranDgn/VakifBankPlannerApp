package com.example.vakifbankplannerapp.presentation.bottomBar

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.vakifbankplannerapp.presentation.navigation.HomeNavGraph

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottombarNavigation() {

    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        bottomBar = {BottomNavBar( navController = navController)},

    ) {

        Box(modifier = Modifier.padding(it)){
            HomeNavGraph(navHostController = navController)
        }
    }

}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Meeting,
        BottomBarScreen.Event,
        BottomBarScreen.Birthday,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any{it.route == currentDestination?.route}

    if(bottomBarDestination){
        BottomNavigation(backgroundColor = Color(0xffFFAE42), elevation = 5.dp){
            screens.forEach{screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                painter = painterResource(id = screen.icon),
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        selectedContentColor = Color.Black,
        unselectedContentColor = Color.Gray,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },

    )
}