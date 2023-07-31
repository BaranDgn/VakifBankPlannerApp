package com.example.vakifbankplannerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.vakifbankplannerapp.presentation.navigation.Graph
import com.example.vakifbankplannerapp.presentation.navigation.RootNavigationGraph
import com.example.vakifbankplannerapp.ui.theme.VakifbankPlannerAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VakifbankPlannerAppTheme{
                RootNavigationGraph(navController = rememberNavController(), startDestination = Graph.AUTHENTICATION)
            }
        }
    }
}