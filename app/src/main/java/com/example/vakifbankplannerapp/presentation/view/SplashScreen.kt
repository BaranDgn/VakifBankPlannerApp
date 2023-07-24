package com.example.vakifbankplannerapp.presentation.meeting

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController : NavController) {
    Splash(navController)
}

@Composable
fun Splash(navController: NavController) {
    val scale = remember {
        androidx.compose.animation.core.Animatable(1f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 5f,
            animationSpec = tween(
                durationMillis = 500,
                easing = {
                    OvershootInterpolator(2f).getInterpolation(it)
                }
            )
        )
        delay(3000L)
        navController.popBackStack()
        navController.navigate("home_graph")
    }
    Box(
        modifier = Modifier
            .background(Color(android.graphics.Color.parseColor("#FFAE42")))
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.vector_active_white),
            contentDescription = "Splash Screen Logo",
            modifier = Modifier.scale(scale.value)
        )
    }

}
