package com.example.vakifbankplannerapp.presentation.meeting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vakifbankplannerapp.data.model.MeetingItem
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankplannerapp.presentation.view.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingScreen(
    navController: NavController,
    meetingViewModel: MeetingViewModel = hiltViewModel()
) {

    val meetings = produceState<Resource<MeetingItem>>(initialValue = Resource.Loading()){
        value = meetingViewModel.loadMeetings()
    }.value
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
                text = { Text("") },
                backgroundColor = Color(0xffFFAE42),
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
                //MeetingOrderByTeam(navController)
                when(meetings){
                    is Resource.Success->{
                        val meeting = meetings.data
                        if (meeting != null) {
                            MeetingOrderByTeam(navController = navController, meetingListem = meeting)

                        }
                    }
                    is Resource.Error->{

                    }
                    is Resource.Loading->{
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeetingOrderByTeam(
    navController: NavController,
    meetingListem : MeetingItem,
    meetingViewModel: MeetingViewModel = hiltViewModel()
) {

    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(meetingListem) { item ->

            val tarih = ZamanArrangement(item.meetingDate).getOnlyDate()

            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToEnd) {
                        //navController.navigate(FeatureScreens.NewMeetingScreen.route)
                        CoroutineScope(Dispatchers.IO).launch{
                            //meetingViewModel.deleteMeeting(DeleteItem(item.id))
                        }

                    }
                    it != DismissValue.DismissedToEnd
                }
            )
            LaunchedEffect(dismissState){
                if (item == meetingListem.first()){
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
                        meetingType = item.meetingName,
                        meetingDate = tarih.tarih,
                        meetingClock = tarih.saat,
                        meetingContent = item.meetingContext,
                        meetingNotes = item.meetingContent,
                        navController = navController
                    )
                }
            )
        }
    }
}



@Composable
@OptIn(ExperimentalMaterialApi::class)
fun SwipeBackground(dismissState: DismissState) {
    val color by animateColorAsState(
        when (dismissState.targetValue) {
            DismissValue.Default -> Color.Transparent
            DismissValue.DismissedToEnd -> Color.White
            DismissValue.DismissedToStart -> Color.White
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