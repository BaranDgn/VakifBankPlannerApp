package com.example.vakifbankplannerapp.presentation.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.data.model.*
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.domain.util.AdminControl
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankkplannerapp.presentation.bottomBar.BottomBarScreen
import com.example.vakifbankplannerapp.presentation.meeting.MeetingViewModel
import com.example.vakifbankplannerapp.presentation.meeting.SwipeBackground
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.updateMeeting.UpdatePopUpForEvent
import com.example.vakifbankplannerapp.presentation.updateMeeting.UpdateViewModel
import com.example.vakifbankplannerapp.presentation.view.EventCardView
import com.example.vakifbankplannerapp.presentation.view.ExpandableFAB
import com.example.vakifbankplannerapp.presentation.view.MainSearchBar
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import com.example.vakifbankplannerapp.presentation.view.SwipeBackground
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventScreen(
    navController: NavHostController,
    eventViewModel : EventViewModel = hiltViewModel(),
    updateViewModel: UpdateViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val events = produceState<Resource<Event>>(initialValue = Resource.Loading()){
        value = eventViewModel.loadEvents()
    }.value

    var selectedEvent by remember { mutableStateOf<EventItem?>(null) }

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

            ExpandableFAB(
                navController = navController,
                floatingActionButtonList = listOf(
                    {
                        if(AdminControl.adminControl){
                            ExtendedFloatingActionButton(
                                text = { Text("Add Event") },
                                icon = {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                },
                                onClick = {
                                    scope.launch {
                                        navController.navigate(FeatureScreens.NewEventScreen.route)
                                    }
                                },
                                modifier = Modifier.padding(bottom = 10.dp),
                                backgroundColor = Color(0xffffae42)
                            )
                        }
                        },
                    {
                        ExtendedFloatingActionButton(
                            text = { Text("Previous Events") },
                            icon = {
                                Icon(painter = painterResource(R.drawable.outline_history_24), contentDescription = "History")
                            },
                            onClick = {
                                scope.launch {
                                    navController.navigate(FeatureScreens.PastEventsScreen.route)
                                }
                            },
                            modifier = Modifier.padding(bottom = 10.dp),
                            backgroundColor = Color(0xffffae42)
                        )
                    }
                )
            )
        }
    ){
        val isLoading by eventViewModel.isLoading.collectAsState()

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { eventViewModel.refreshEvents(navController) }
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff2f2f2))
                .padding(it)
        ) {
            Spacer(modifier = Modifier
                .padding()
                .height(16.dp))
            when(events){
                is Resource.Success->{
                    val event = events.data
                    if (event != null) {
                        EventListing(navController, event)
                    }

                }
                is Resource.Error->{

                }
                is Resource.Loading->{
                    CircularProgressIndicator(color = Color.DarkGray, modifier = Modifier.align(
                        Alignment.CenterHorizontally))

                }

            }




        }}
    }
}

@OptIn(ExperimentalMaterialApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListing(
    navController: NavController,
    eventList : Event,
    eventViewModel: EventViewModel = hiltViewModel(),
    updateViewModel: UpdateViewModel = hiltViewModel(),
    meetingViewModel : MeetingViewModel = hiltViewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    // Define a variable to store the ID of the item to be deleted
    var itemToDelete by remember { mutableStateOf<DeleteItem?>(null) }

    var selectedEvent by remember { mutableStateOf<EventItem?>(null) }


    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(eventList){event->
            val tarih = ZamanArrangement(event.eventDateTime).getOnlyDate()


            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        //navController.navigate(FeatureScreens.NewMeetingScreen.route)
                        CoroutineScope(Dispatchers.IO).launch{
                            itemToDelete = DeleteItem(event.id)
                            showDeleteDialog = true
                            meetingViewModel.refreshMeetings(navController, BottomBarScreen.Event.route)
                        }
                    }
                    it != DismissValue.DismissedToEnd
                }
            )

            LaunchedEffect(dismissState){
                if (!eventViewModel.didAnimationExecute.value){
                    if (event == eventList.first()){
                        dismissState.animateTo(
                            DismissValue.DismissedToStart,
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
                    eventViewModel.didAnimationExecute.value = true
                }
            }
            if(AdminControl.adminControl){
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd),
                    dismissThresholds = { direction ->
                        FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
                    },
                    background = { SwipeBackground(dismissState = dismissState) },
                    dismissContent = {
                        EventCardView(
                            eventName = event.eventName,
                            eventType= event.eventType,
                            eventDateTime= tarih.tarih,
                            eventHour = tarih.saat,
                            meetingNotes= event.eventNotes,
                            navController = navController,
                            onEditClicked = { selectedEvent = event})
                    }
                )
            }else{
                EventCardView(
                    eventName = event.eventName,
                    eventType= event.eventType,
                    eventDateTime= tarih.tarih,
                    eventHour = tarih.saat,
                    meetingNotes= event.eventNotes,
                    navController = navController,
                    onEditClicked = { selectedEvent = event})
            }

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { direction ->
                    FractionalThreshold(0.3f)
                },
                background = { SwipeBackground(dismissState = dismissState) },
                dismissContent = {
                    EventCardView(
                        eventName = event.eventName,
                        eventType= event.eventType,
                        eventDateTime= tarih.tarih,
                        eventHour = tarih.saat,
                        meetingNotes= event.eventNotes,
                        navController = navController,
                        onEditClicked = { selectedEvent = event})
                }
            )

            //Alert Dialog to delete event
            if (showDeleteDialog && itemToDelete != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        itemToDelete = null
                    },
                    title = { Text("Delete Item") },
                    text = { Text("Are you sure you want to delete this item?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                // Delete the item from the list and close the dialog
                                itemToDelete?.let {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        eventViewModel.deleteSelectedEvent(DeleteEvent(deleteId = it.deleteId))
                                        meetingViewModel.refreshMeetings(navController,BottomBarScreen.Event.route)
                                    }
                                }
                                showDeleteDialog = false
                                itemToDelete = null
                            }

                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                // Close the dialog without deleting the item
                                showDeleteDialog = false
                                itemToDelete = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Show the update pop-up when a meeting is selected
            selectedEvent?.let { event ->
                UpdatePopUpForEvent(
                    event = event,
                    onDismiss = { selectedEvent = null },
                    onUpdate = { updatedMeeting ->
                        CoroutineScope(Dispatchers.IO).launch{
                            updateViewModel.updateEvent(updateEvent = updatedMeeting)
                            meetingViewModel.refreshMeetings(navController,BottomBarScreen.Event.route)
                        }
                        selectedEvent = null
                    }
                )
            }

        }
    }
}

fun deleteItemFromList(item: DeleteItem, list: Event) {

}
