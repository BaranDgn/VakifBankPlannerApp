package com.example.vakifbankplannerapp.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EventCardView(
     eventName: String,
     eventType: String,
     eventDateTime: String,
     eventHour : String,
     meetingNotes: String,
    navController: NavController,
     onEditClicked: ()-> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
       ,
        onClick = {onEditClicked()},
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        backgroundColor = Color.White// Set the card's background color here
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
                            text = eventType,
                            style = TextStyle(color = Color.Black, fontSize = 19.sp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Event Notes: ",
                            style = TextStyle(color = Color.Black, fontSize = 14.sp)
                        )
                        Text(
                            text = meetingNotes,
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
                            text = eventDateTime,
                            style = TextStyle(color = Color.Black, fontSize = 18.sp)
                        )
                        Text(
                            text = eventHour,
                            style = TextStyle(color = Color.Black, fontSize = 18.sp)
                        )

                    }
                }

            }
        }
    }
}




