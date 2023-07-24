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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.data.model.Events
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.view.EventCardView
import com.example.vakifbankplannerapp.presentation.view.MainSearchBar
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import kotlinx.coroutines.launch

@Composable
fun EventScreen(
    navController: NavHostController,
    eventViewModel : EventViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val searchWidgetState by eventViewModel.searchWidgetStateForEvent
    val searchTextState by eventViewModel.searchTextStateForEvent
    Scaffold(
        topBar = {
            MainSearchBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    eventViewModel.updateSearchTextStateForEvent(newValue = it)
                },
                onCloseClicked = {
                    eventViewModel.updateSearchWidgetStateForEvent(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {},
                onSearchTriggered = { eventViewModel.updateSearchWidgetStateForEvent(newValue = SearchWidgetState.OPENED)
                },
                text = "Events"
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Event") },
                backgroundColor =Color(0xFF4E4F50),
                icon = {Icon(Icons.Filled.Add, contentDescription = "")},
                onClick ={
                    //scope.launch {
                        navController.navigate(FeatureScreens.NewEventScreen.route)
                   // }
                    scope.launch {
                        val result = snackbarHostState
                            .showSnackbar(
                                message = "Snackbar",
                                actionLabel = "",
                                duration = SnackbarDuration.Indefinite
                            )
                        when(result){
                            SnackbarResult.ActionPerformed -> {

                            }
                            SnackbarResult.Dismissed ->{

                            }
                        }
                    }

                },

                )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff2f2f2))
                .padding(it)
        ) {
            Spacer(modifier = Modifier
                .padding()
                .height(16.dp))
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
        "10.30",
        "celebrating birthday",
        "Only team 1's employee must attend",
    ),
    Events(
        "Happy Hour",
        "Team 2",
        "10.04.2024",
        "10.30",
        "Drinking",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "10.30",
        "Enjoy",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "10.30",
        "Enjoy",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "10.30",
        "Enjoy",
        "Every one can attend",
    ),
    Events(
        "Halloween" ,
        "IT",
        "10.10.2024",
        "10.30",
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
                eventClock = it.eventClock,
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