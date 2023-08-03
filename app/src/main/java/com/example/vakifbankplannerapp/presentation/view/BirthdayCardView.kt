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
import androidx.compose.ui.res.stringResource
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
        Image(
            painter = painterResource(R.drawable.birthday_img_two),
            contentDescription = null,
            modifier = Modifier
                .height(160.dp),
            contentScale = ContentScale.FillBounds
        )
        Row(modifier = Modifier
            .height(160.dp)
            .padding(bottom = 32.dp)
            ){
            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(1.5f)
                .padding(bottom = 8.dp, start = 8.dp),
                contentAlignment = Alignment.Center
            ){

                Text(
                    text = fullName.uppercase(),
                    style = TextStyle(color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                )
            }
            Box(modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .padding(8.dp),
                contentAlignment = Alignment.Center
            ){

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = birthday,
                        style = TextStyle(color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Normal)
                    )
                    Text(
                        text = "$howManyDayLeft ${stringResource(id = R.string.days_left)}",
                        style = TextStyle(color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Light)
                    )
                }
            }
        }
    }
}
