package com.example.vakifbankplannerapp.presentation.pastMeeting

import android.os.Build
import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.Meeting
import com.example.vakifbankplannerapp.data.model.MeetingItem
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankplannerapp.presentation.view.MainSearchBar
import com.example.vakifbankplannerapp.presentation.view.MeetingCardView
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastMeetingScreen(
    navController: NavController,
    pastMeetingViewModel: PastMeetingViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val pastMeetings by pastMeetingViewModel.pastMeetingList
    val searchWidgetState by pastMeetingViewModel.searchWidgetState
    val searchTextState by pastMeetingViewModel.searchTextState

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit){
        pastMeetingViewModel.loadPastMeetings()
    }

    Scaffold(
        topBar = {
            MainSearchBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    pastMeetingViewModel.updateSearchTextState(newValue = it)
                },
                onCloseClicked = {
                    pastMeetingViewModel.updateSearchWidgetState(newValue = SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    pastMeetingViewModel.searchBasedOnTeamName(searchTextState)
                },
                onSearchTriggered = { pastMeetingViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                },
                text="Geçmiş Toplantılar",
                includeBack = true,
                navController = navController
            )
        },
    ){
        val isLoading by pastMeetingViewModel.isLoading.collectAsState()

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = pastMeetingViewModel::loadStuff
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xfff2f2f2))
                    .padding(it)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                when(pastMeetings){
                    is Resource.Success->{
                        val pastMeeting = pastMeetings.data
                        if(pastMeeting != null) {
                            PastMeetingList(navController = navController, meetingListem = pastMeeting)
                        }
                    }
                    is Resource.Error->{
                        Toast.makeText(context, "There is no Meeting to Show", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading->{
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.align(
                            Alignment.CenterHorizontally))
                    }

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PastMeetingList(
    navController: NavController,
    meetingListem : MeetingItem,
) {
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(meetingListem) { item ->

            val tarih = ZamanArrangement(item.meetingDate).getOnlyDate()

            MeetingCardView(
                meetingName = item.teamName,
                meetingType = item.meetingName,
                meetingDate = tarih.tarih,
                meetingClock = tarih.saat,
                meetingContent = item.meetingContext,
                meetingNotes = item.meetingContent,
                navController = navController,
                onEditClicked = {item.id}
            )
        }
    }
}