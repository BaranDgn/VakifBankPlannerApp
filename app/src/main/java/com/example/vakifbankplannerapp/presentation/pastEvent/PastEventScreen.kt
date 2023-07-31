package com.example.vakifbankplannerapp.presentation.pastEvent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.data.model.EventItem
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankplannerapp.presentation.view.EventCardView
import com.example.vakifbankplannerapp.presentation.view.MainSearchBar
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastEventScreen(
    navController: NavController,
    pastEventViewModel: PastEventViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

    val events = produceState<Resource<Event>>(initialValue = Resource.Loading()){
        value = pastEventViewModel.loadPastEvents()
    }.value

    val searchWidgetState by pastEventViewModel.searchWidgetStateForEvent
    val searchTextState by pastEventViewModel.searchTextStateForEvent
    Scaffold(
        topBar = {
            MainSearchBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    pastEventViewModel.updateSearchTextStateForEvent(newValue = it)
                },
                onCloseClicked = {
                    pastEventViewModel.updateSearchWidgetStateForEvent(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {},
                onSearchTriggered = { pastEventViewModel.updateSearchWidgetStateForEvent(newValue = SearchWidgetState.OPENED)
                },
                text = "Past Events",
                includeBack = true,
                navController = navController
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
                        PastEventListing(navController, event)
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
fun PastEventListing(
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