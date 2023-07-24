package com.example.vakifbankplannerapp.presentation.birthday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.ui.theme.Great_Vibes
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


@Composable
fun BirthdayScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor("#FFAE42"))),
        ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color(android.graphics.Color.parseColor("#d3d3d3cc")))
        ){
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Text(
                    "Doğum Günün Kultu Olsun",
                    fontSize = 50.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = Great_Vibes
                )
                Text(
                    "John Doe",
                    fontSize = 30.sp,
                    textAlign = TextAlign.Center,
                   fontFamily = Great_Vibes
                )
            }
        }
    }
    KonfettiView(
        modifier = Modifier.fillMaxSize(),
        parties = listOf(
            Party(
                speed = 0f,
                maxSpeed = 30f,
                damping = 0.9f,
                spread = 360,
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                position = Position.Relative(0.5, 0.0),
                emitter = Emitter(duration = 1, TimeUnit.SECONDS).max(100)
            )
        )
    )
}

@Preview
@Composable
fun BirthdayScreenPreview(){
    BirthdayScreen()
}