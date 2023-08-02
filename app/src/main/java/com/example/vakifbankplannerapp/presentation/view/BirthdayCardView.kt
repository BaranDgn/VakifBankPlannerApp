package com.example.vakifbankplannerapp.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BirthdayCarView(
    fullName : String,
    birthday : String,
    howManyDayLeft : String
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        onClick = {},
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp,
        backgroundColor = Color.White// Set the card's background color here
    ) {

        Row(modifier = Modifier
            .height(160.dp)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray
                    )
                )
            )){
            Image(
                painter = painterResource(R.drawable.birthday_img_two),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(2f)
                .padding(8.dp),
                contentAlignment = Alignment.Center
            ){

                Text(
                    text = "BARAN DOĞAN",
                    style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                )
            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp),
                contentAlignment = Alignment.Center
            ){

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "10/05/1998",
                        style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Normal)
                    )
                    Text(
                        text = "19 gün kaldı",
                        style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.Light)
                    )
                }
            }
        }
    }
}
