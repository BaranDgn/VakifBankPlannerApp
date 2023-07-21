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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
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
    eventContent : String,
    eventNotes: String,
    navController: NavController
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {


            },
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier
            .height(160.dp)
            .background(Color.White)){

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ){
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)) {


                    Text(
                        text = eventName,
                        style = TextStyle(color = Color.Black, fontSize = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = eventTeam,
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                    Text(
                        text = eventDate,
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                    Text(
                        text = eventContent,
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                    Text(
                        text = eventNotes,
                        style = TextStyle(color = Color.Black, fontSize = 16.sp)
                    )
                }



            }
        }
    }



}



