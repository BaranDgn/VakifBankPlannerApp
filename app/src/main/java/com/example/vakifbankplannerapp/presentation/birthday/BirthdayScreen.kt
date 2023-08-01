package com.example.vakifbankplannerapp.presentation.birthday

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vakifbankplannerapp.data.model.Birthday
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.ui.theme.Great_Vibes
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


@Composable
fun BirthdayScreen(
    birthdayViewModel: BirthdayViewModel = hiltViewModel()
) {
    val birthdays = produceState<Resource<Birthday>>(initialValue = Resource.Loading()){
        value = birthdayViewModel.loadBirthdays()
    }.value

    var hasBirhtyday by remember { mutableStateOf(false) }

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
            when(birthdays){
                is Resource.Success->{
                    val birthday = birthdays.data
                    if (birthday != null) {
                        if (birthday.size > 0) {
                            hasBirhtyday = true
                            Column  (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.SpaceAround,
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ){
                                Text(
                                    "Doğum Günün Kultu Olsun",
                                    fontSize = 50.sp,
                                    textAlign = TextAlign.Center,
                                    fontFamily = Great_Vibes
                                )
                                BirthdayListing(birthday =birthday)
                            }
                        } else {
                            Text(
                                "Bugün Doğum Günü Olan Yok",
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = Great_Vibes
                            )
                        }
                    }
                    else{
                        Text(
                            "Bugün Doğum Günü Olan Yok",
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = Great_Vibes
                        )
                    }
                }
                is Resource.Error->{

                }
                is Resource.Loading->{
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
    if (hasBirhtyday){
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

@Composable
fun BirthdayListing(birthday: Birthday) {
    LazyColumn {
        items(birthday) { birthdayItem ->
            Text(
                text = "${birthdayItem.name} ${birthdayItem.surname}",
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontFamily = Great_Vibes
            )
        }
    }
}

@Preview
@Composable
fun BirthdayScreenPreview(){
    BirthdayScreen()
}