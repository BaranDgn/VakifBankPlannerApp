package com.example.vakifbankplannerapp.presentation.view

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeetingCardView(
    //meetingList: List<Teams>,
    meetingName: String,
    meetingType: String,
    meetingDate: String,
    meetingClock: String,
    meetingContent: String,
    meetingNotes: String,
    navController: NavController,
    onEditClicked :() -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var hasMore by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
         ,
        onClick = {onEditClicked()},
        shape = RoundedCornerShape(8.dp),
        elevation = 5.dp
    ) {
        Column(modifier = Modifier
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.White,
                        Color.LightGray

                    )
                )
            )
        ){

            Box(modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ){

                Column(
                    modifier = Modifier
                        .animateContentSize()
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
                                text = meetingName,
                                style = TextStyle(color = Color.Black, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = meetingType,
                                style = TextStyle(color = Color.Black, fontSize = 19.sp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Meeting Content: ",
                                style = TextStyle(color = Color.Black, fontSize = 14.sp)
                            )
                            Text(
                                text = meetingContent,
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
                                text = meetingDate,
                                style = TextStyle(color = Color.Black, fontSize = 18.sp)
                            )
                            Text(
                                text = meetingClock,
                                style = TextStyle(color = Color.Black, fontSize = 18.sp)
                            )

                        }
                    }
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(),
                        text = "Meeting Notes: ",
                        style = TextStyle(color = Color.Black, fontSize = 14.sp,  textDecoration = TextDecoration.Underline)
                    )

                    Text(
                        text = meetingNotes,
                        style = TextStyle(color = Color.Black, fontSize = 17.sp),
                        maxLines = if (expanded) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.animateContentSize(
                            animationSpec = tween(
                                durationMillis = 300,
                                easing = LinearOutSlowInEasing
                            )
                        ),
                        onTextLayout = {
                            if(it.hasVisualOverflow){
                                hasMore = true
                            }
                        }
                    )
                    if (hasMore){
                        IconButton (
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .fillMaxWidth()
                                .alpha(0.5f)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = "Expandable Icon",
                                tint = Color.Black,
                                modifier = Modifier
                                    .alpha(0.5f)
                                    .rotate(if (expanded) 180f else 0f)
                            )
                        }
                    }
                }
            }
        }
    }

}