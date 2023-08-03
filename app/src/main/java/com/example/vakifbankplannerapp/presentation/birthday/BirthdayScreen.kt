package com.example.vakifbankplannerapp.presentation.birthday

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.Birthday
import com.example.vakifbankplannerapp.data.model.IncomingBirthday
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.presentation.view.BirthdayCarView
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

    val incomingBirthdays = produceState<Resource<IncomingBirthday>>(initialValue = Resource.Loading()){
        value = birthdayViewModel.loadIncomingBirthdays()
    }.value

    var tabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Today", "Incoming")

    var hasBirhtyday by remember { mutableStateOf(false) }

    val activity = LocalContext.current as Activity
    val window = activity.window

    /*DisposableEffect(key1 = , effect = ) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        // if this window opens setDecorFitsSystemWindows to false, make other windows don't affect
        if (window.decorView.rootWindowInsets != null) {
            val insetsController = WindowInsetsControllerCompat(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = true
            window.statusBarColor = Color.Transparent.toArgb()
        }
        *//*WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.isAppearanceLightStatusBars = true
            window.statusBarColor = Color.Transparent.toArgb()
        }*//*
    }*/
    val insetsController = WindowInsetsControllerCompat(window, window.decorView)
    DisposableEffect(key1 = window) {
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        if (window.decorView.rootWindowInsets != null) {
            insetsController.hide(WindowInsetsCompat.Type.statusBars())
            window.statusBarColor = Color.Transparent.toArgb()
        }
        onDispose {
            //        WindowCompat.setDecorFitsSystemWindows(window, true)
            insetsController.show(WindowInsetsCompat.Type.statusBars())
            window.statusBarColor = ContextCompat.getColor(activity,R.color.colorPrimary)
        }
    }


    @Composable
    fun todayBirthdayList(){
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
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            Text(
                                "Doğum Günün Kultu Olsun",
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = Great_Vibes,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            BirthdayListing(birthday =birthday)
                        }
                    } else {
                        hasBirhtyday = false
                        Column  (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                "Bugün Doğum Günü Olan Yok",
                                fontSize = 50.sp,
                                textAlign = TextAlign.Center,
                                fontFamily = Great_Vibes
                            )
                        }
                    }
                }
                else{
                    hasBirhtyday = false
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
                CircularProgressIndicator(color = Color.Black)
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
    }

    @Composable
    fun incomingBirhtdayList(){
        when (incomingBirthdays){
            is Resource.Success->{
                val incomingBirthday = incomingBirthdays.data
                if (incomingBirthday != null) {
                    if (incomingBirthday.size > 0) {
                        Column  (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ){
                            IncomingBirthdayListing(incomingBirthday =incomingBirthday)
                        }
                    } else {
                        Text(
                            "No Birthday in 1 Month",
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            fontFamily = Great_Vibes
                        )
                    }
                }
                else{
                    Text(
                        "No Birthday in 1 Month",
                        fontSize = 50.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = Great_Vibes
                    )
                }
            }
            is Resource.Error->{

            }
            is Resource.Loading->{
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
        ) {
        Image(
            painter = painterResource(R.drawable.birthday_img_seven_),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(30.dp)
                .clip(shape = RoundedCornerShape(20.dp))
                .background(Color.Transparent),
            ){
            Column(modifier = Modifier.fillMaxWidth()) {
                TabRow(
                    selectedTabIndex = tabIndex,
                    backgroundColor = Color.Transparent,

                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index },
                            unselectedContentColor = Color.Black,
                        )
                    }
                }
                when (tabIndex) {
                    0 -> todayBirthdayList()
                    1 -> incomingBirhtdayList()
                }
            }
        }
    }
    /*KonfettiView(
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
    )*/
}

@Composable
fun BirthdayListing(birthday: Birthday) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

@Composable
fun IncomingBirthdayListing(incomingBirthday: IncomingBirthday) {
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(incomingBirthday) { incomingBirthdayItem ->
            BirthdayCarView(
                fullName = "${incomingBirthdayItem.name} ${incomingBirthdayItem.surname}",
                birthday = "${incomingBirthdayItem.birthDay}",
                howManyDayLeft = "${incomingBirthdayItem.counterOfDay.toString()}"
            )
        }
    }
}

@Preview
@Composable
fun BirthdayScreenPreview(){
    BirthdayScreen()
}