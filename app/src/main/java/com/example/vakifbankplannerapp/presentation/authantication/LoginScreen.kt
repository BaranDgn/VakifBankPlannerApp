package com.example.vakifbankplannerapp.presentation.authantication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.presentation.navigation.Graph

@Composable
fun LoginScreeen(
    navController: NavController,
   ) {
    //val navController = rememberNavController()
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ){
            Text(text ="Login", modifier = Modifier.clickable {
                navController.navigate(Graph.HOME)
            })

    }
}