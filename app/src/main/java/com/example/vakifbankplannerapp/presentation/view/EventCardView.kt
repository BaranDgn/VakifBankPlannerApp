package com.example.vakifbankplannerapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Events
import com.example.vakifbankplannerapp.data.model.Teams
import okhttp3.internal.wait

@Composable
fun EventCardView(
    //EventList : List<Events>,
    eventName : String,
    eventTeam: String,
    eventDate : String,
    eventClock: String,
    eventContent : String,
    eventNotes: String,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {},
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier
            .height(160.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray

                    )
                )
            )){

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ){
                Row(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier
                        .weight(2f)
                        //.fillMaxSize()
                        .padding(4.dp),
                        verticalArrangement = Arrangement.SpaceEvenly
                        //, horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = eventName,
                            style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = eventTeam,
                            style = TextStyle(color = Color.Black, fontSize = 19.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Event Content: ",
                            style = TextStyle(color = Color.Black, fontSize = 14.sp)
                        )
                        Text(
                            text = eventContent,
                            style = TextStyle(color = Color.Black, fontSize = 17.sp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Event Notes: ",
                            style = TextStyle(color = Color.Black, fontSize = 14.sp)
                        )
                        Text(
                            text = eventNotes,
                            style = TextStyle(color = Color.Black, fontSize = 17.sp)
                        )
                    }

                    Column(modifier = Modifier
                        // .fillMaxSize()
                        .padding(4.dp)
                        .weight(1f)
                        , verticalArrangement = Arrangement.Center
                        // , horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = eventDate,
                            style = TextStyle(color = Color.Black, fontSize = 18.sp)
                        )
                        Text(
                            text = eventClock,
                            style = TextStyle(color = Color.Black, fontSize = 18.sp)
                        )

                    }
                }

            }
        }
    }
}



