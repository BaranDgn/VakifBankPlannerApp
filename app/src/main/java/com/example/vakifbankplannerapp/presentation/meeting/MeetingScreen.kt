package com.example.vakifbankplannerapp.presentation.meeting

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Teams
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vakifbankplannerapp.presentation.view.*
import kotlinx.coroutines.launch


@Composable
fun MeetingScreen(
    navController: NavController,
    meetingViewModel: MeetingViewModel = viewModel()
) {

    val searchWidgetState by meetingViewModel.searchWidgetState
    val searchTextState by meetingViewModel.searchTextState

    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
                 MainSearchBar(
                     searchWidgetState = searchWidgetState,
                     searchTextState = searchTextState,
                     onTextChange = {
                                    meetingViewModel.updateSearchTextState(newValue = it)
                     },
                     onCloseClicked = {
                                      meetingViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                     },
                     onSearchClicked = {},
                     onSearchTriggered = { meetingViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                     },
                     text="Meetings"
                 )
        },

        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Meeting") },
                icon = {Icon(Icons.Filled.Add, contentDescription = "")},
                backgroundColor =Color(0xFF4E4F50),// Color(0xFFE2DED0),
                onClick ={
                        scope.launch {
                            navController.navigate(FeatureScreens.NewMeetingScreen.route)
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

            Spacer(modifier = Modifier.height(16.dp))
           // MeetingItems("Team 1")
            //Meetin Items
            MeetingOrderByTeam(navController)

            Text("GoTo", modifier = Modifier.clickable { navController.navigate(FeatureScreens.NewMeetingScreen.route) })
        }


    }
}



var myList = listOf(
    Teams(
        "Team 1",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 4",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Every team's employee must attend",
    )
)
@Composable
fun MeetingOrderByTeam(
    navController: NavController
) {
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(myList){
            MeetingCardView(
             meetingName = it.teamName,
             meetingType =  it.meetingType,
             meetingDate =  it.meetingDate,
             meetingClock =  it.meetingClock,
             meetingContent = it.meetingContent,
             meetingNotes = it.meetingNotes,
                navController = navController )
        }
    }
}

@Composable
fun SearchBarForMeeting(
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

@Composable
fun FloatingActionButton(
    navController: NavController
) {
    ExtendedFloatingActionButton(
        text = {
            Text(text = "Navigate", color = Color.White)
        },
        icon = {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Navigate FAB",
                tint = Color.White,
            )
        },

        onClick = { navController.navigate(FeatureScreens.NewMeetingScreen.route) },

        //containerColor = colors.secondaryVariant,
    )

    @Composable
    fun ExampleScreen(
        floatingViewModel: ExtendedFloatingViewModel
    ) {
        val context = LocalContext.current
        val listState = rememberLazyListState()
        val expandedFabState = remember {
            derivedStateOf {
                listState.firstVisibleItemIndex == 0
            }
        }

        LaunchedEffect(key1 = expandedFabState.value) {
            floatingViewModel.expandedFab.value = expandedFabState.value
        }

        LaunchedEffect(key1 = Unit) {
            floatingViewModel.fabOnClick.value = {
                Toast.makeText(context, "Settings FAB Clicked", Toast.LENGTH_SHORT).show()
            }

        }

    }
}