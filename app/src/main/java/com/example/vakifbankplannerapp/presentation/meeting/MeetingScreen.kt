package com.example.vakifbankplannerapp.presentation.meeting

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Teams
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vakifbankplannerapp.presentation.view.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay
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
                backgroundColor =Color.LightGray,
                icon = {Icon(Icons.Filled.Add, contentDescription = "")},
                onClick ={
                    scope.launch {
                        navController.navigate(FeatureScreens.NewMeetingScreen.route)
                    }

                },

                )
        }
    ){
        val isLoading by meetingViewModel.isLoading.collectAsState()
        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = meetingViewModel::loadStuff
        ) {
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
}

var myList = listOf(
    Teams(
        "Team 1",
        "Daily meeting",
        "04.04.2023",
        "10.30",
        "Talking about what it is done",
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi vitae sagittis magna, eget molestie est. Vivamus et tempus orci, ut posuere odio. Proin non purus auctor, interdum ex eget, facilisis tortor. Vivamus porta ultricies mollis. Sed sit amet cursus erat, at molestie tellus. Quisque vitae congue mi. Nullam non diam mi. Nam tempor sit amet odio a tempor. Maecenas aliquam lorem vel dolor iaculis, vel ultrices tortor posuere. Proin non dolor vel nulla luctus tempus ut quis magna. Etiam urna massa, venenatis quis nisl quis, tincidunt pellentesque neque. Cras a malesuada tellus. Cras id semper felis, in aliquam mauris. Cras id consectetur est.",
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeetingOrderByTeam(
    navController: NavController
) {
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(
            items = myList
        ) { item ->
            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd) {
                        navController.navigate(
                            FeatureScreens.NewMeetingScreen.route
                        )
                    } else {
                        false
                    }
                    it != DismissValue.DismissedToEnd
                }
            )
            LaunchedEffect(dismissState){
                if (item == myList.first()){
                    dismissState.animateTo(
                        DismissValue.DismissedToEnd,
                        anim = tween(
                            durationMillis = 400,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    delay(100)
                    dismissState.animateTo(
                        DismissValue.Default,
                        anim = tween(
                            durationMillis = 400,
                            easing = LinearOutSlowInEasing
                        )
                    )
                }
            }
            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.StartToEnd),
                dismissThresholds = { direction ->
                    FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                },
                background = { SwipeBackground(dismissState = dismissState) },
                dismissContent = {
                    MeetingCardView(
                        meetingName = item.teamName,
                        meetingType = item.meetingType,
                        meetingDate = item.meetingDate,
                        meetingClock = item.meetingClock,
                        meetingContent = item.meetingContent,
                        meetingNotes = item.meetingNotes,
                        navController = navController
                    )
                }
            )
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

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SwipeBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.Transparent
            DismissValue.DismissedToEnd -> Color.Green
            DismissValue.DismissedToStart -> Color.Red
        }
    )
    val alignment = Alignment.CenterStart

    val icon = Icons.Default.Done

    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}