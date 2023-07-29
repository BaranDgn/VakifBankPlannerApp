package com.example.vakifbankplannerapp.presentation.event

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.view.EventCardView
import com.example.vakifbankplannerapp.presentation.view.MainSearchBar
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventScreen(
    navController: NavHostController,
    eventViewModel : EventViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val events = produceState<Resource<Event>>(initialValue = Resource.Loading()){
        value = eventViewModel.loadEvents()
    }.value

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
                text = { Text("") },
                backgroundColor = Color(0xffFFAE42),
                icon = {Icon(Icons.Filled.Add, contentDescription = "")},
                onClick ={
                    scope.launch {
                        navController.navigate(FeatureScreens.NewEventScreen.route)
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
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.align(
                        Alignment.CenterHorizontally))

                }

            }


        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventListing(
    navController: NavController,
    eventList : Event
) {

    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(eventList){event->
            val tarih = ZamanArrangement(event.eventDateTime).getOnlyDate()
            EventCardView(
                eventName = event.eventName,
                eventType= event.eventType,
                eventDateTime= tarih.tarih,
                eventHour = tarih.saat,
                meetingNotes= event.eventNotes,
                navController = navController)
        }
    }
}
