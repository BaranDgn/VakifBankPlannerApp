package com.example.vakifbankplannerapp.presentation.event

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.data.model.Events
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.view.EventCardView

@Composable
fun EventScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Events", color = Color.Black)},
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "",
                            tint = Color.White)
                    }
                },
                backgroundColor = Color(0xffFFAE42),
                contentColor = Color.Black,
                elevation = 10.dp,
                modifier = Modifier.height(60.dp)
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize().background(Color(0xfff2f2f2))
                .padding(it)
        ) {
            SearchBarForEvent()
            Spacer(modifier = Modifier
                .padding()
                .height(16.dp))
            Text("GoTo", modifier = Modifier.clickable { navController.navigate(FeatureScreens.NewEventScreen.route) })

            EventListing(navController)

        }
        it
    }

}

@Composable
fun EventItem() {

}
var myList = listOf(
    Events(
        "Birthday Party",
        "Team 1",
        "10.04.2024",
        "celebrating birthday",
        "Only team 1's employee must attend",
    ),
    Events(
        "Happy Hour",
        "Team 2",
        "10.04.2024",
        "Drinking",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "Enjoy",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "Enjoy",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "Enjoy",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "Enjoy",
        "Every one can attend",
    ),

    )
@Composable
fun EventListing(
    navController: NavController
) {

    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(myList){
            EventCardView(
                eventName = it.eventName,
                eventTeam = it.eventTeam,
                eventDate = it.eventDate,
                eventContent = it.eventContent,
                eventNotes = it.eventNotes,
                navController = navController)
        }
    }

}

@Composable
fun SearchBarForEvent(
    modifier: Modifier = Modifier,
    hint:String = "",
    onSearch : (String) -> Unit = {}
){
    var text by remember{
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier){
        BasicTextField(value =  text, onValueChange ={
            text = it
            onSearch(it)
        },  maxLines = 1,
            singleLine = true,
            textStyle = TextStyle.Default,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }
        )
        if(isHintDisplayed){
            Text(text = hint, color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }
}