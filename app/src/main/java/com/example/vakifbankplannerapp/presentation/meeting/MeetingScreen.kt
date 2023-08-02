package com.example.vakifbankplannerapp.presentation.meeting

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.Birthday
import com.example.vakifbankplannerapp.data.model.DeleteItem
import com.example.vakifbankplannerapp.data.model.Meeting
import com.example.vakifbankplannerapp.data.model.MeetingItem
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankplannerapp.presentation.authantication.LoginViewModel
import com.example.vakifbankplannerapp.presentation.bottomBar.BottomBarScreen
import com.example.vakifbankplannerapp.presentation.updateMeeting.MeetingUpdatePopup
import com.example.vakifbankplannerapp.presentation.updateMeeting.UpdateViewModel
import com.example.vakifbankplannerapp.presentation.view.*
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingScreen(
    navController: NavController,
    meetingViewModel: MeetingViewModel = hiltViewModel(),
    isBirthday: Boolean = false
    loginViewModel: LoginViewModel= hiltViewModel()
) {
    val context = LocalContext.current

    val meetings = produceState<Resource<MeetingItem>>(initialValue = Resource.Loading()){
        value = meetingViewModel.loadMeetings()
    }.value
    val searchWidgetState by meetingViewModel.searchWidgetState
    val searchTextState by meetingViewModel.searchTextState

    var meetingOfList by remember { meetingViewModel.meetingList }

    val scope = rememberCoroutineScope()

    val isAdminCheck = loginViewModel.isAdmin

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
                     onSearchClicked = {
                        meetingViewModel.searchBasedOnTeamName(searchTextState)
                     },
                     onSearchTriggered = { meetingViewModel.updateSearchWidgetState(newValue = SearchWidgetState.OPENED)
                     },
                     text="Meetings"
                 )
        },

        floatingActionButton = {
           ExpandableFAB(
               navController = navController,
               floatingActionButtonList = listOf(
                   {
                       if(loginViewModel.isAdminCheck(isAdminCheck.value)){
                           ExtendedFloatingActionButton(
                               text = { Text("Add Meeting") },
                               icon = {
                                   Icon(Icons.Default.Add, contentDescription = "Add")
                               },
                               onClick = {
                                   scope.launch {
                                       navController.navigate(FeatureScreens.NewMeetingScreen.route)
                                   }
                               },

                               modifier = Modifier.padding(bottom = 10.dp),
                               backgroundColor = Color(0xffffae42)
                           )
                       }

                   },
                   {
                       ExtendedFloatingActionButton(
                           text = { Text("Previous Meetings") },
                           icon = {
                               Icon(painter = painterResource(R.drawable.outline_history_24), contentDescription = "History")
                           },
                           onClick = {
                               scope.launch {
                                   navController.navigate(FeatureScreens.PastMeetingScreen.route)
                               }
                           },
                           modifier = Modifier.padding(bottom = 10.dp),
                           backgroundColor = Color(0xffffae42)
                       )
                   }
               )
           )
       },



    ){
        val isLoading by meetingViewModel.isLoading.collectAsState()

        val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isLoading)
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { meetingViewModel.refreshMeetings(navController, BottomBarScreen.Meeting.route) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xfff2f2f2))
                    .padding(it)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                //MeetingOrderByTeam(navController = navController, meetingListem = meetingOfList)
                when(meetings){

                    is Resource.Success->{
                        val meeting = meetings.data
                        if (meeting != null) {
                            MeetingOrderByTeam(navController = navController, meetingListem = meeting)
                            val meetingItems = meeting.mapIndexed { index, meetingi ->
                                Meeting(
                                    meetingi.id,
                                    meetingi.isMeeting,
                                    meetingi.meetingContent,
                                    meetingi.meetingContext,
                                    meetingi.meetingDate,
                                    meetingi.meetingName,
                                    meetingi.meetingTime,
                                    meetingi.teamName,
                                )
                            }
                            meetingOfList += meetingItems
                        }
                    }
                    is Resource.Error->{
                        Toast.makeText(context, "There is no Meeting to Show", Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading->{
                        CircularProgressIndicator(color = Color.Black, modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                }
            }
        }

        /*if(isBirthday){
            // show just a popup to congratulate
            Dialog(onDismissRequest = {  }) {
                Surface {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Happy Birthday!")
                    }
                }
            }
        }*/
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MeetingOrderByTeam(
    navController: NavController,
    meetingListem : List<Meeting>,
    meetingViewModel: MeetingViewModel = hiltViewModel(),
    updateViewModel: UpdateViewModel = hiltViewModel()
) {

    var showDeleteDialog by remember { mutableStateOf(false) }
    // Define a variable to store the ID of the item to be deleted
    var itemToDelete by remember { mutableStateOf<DeleteItem?>(null) }

    var selectedMeeting by remember { mutableStateOf<Meeting?>(null) }

    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(meetingListem) { item ->

            val tarih = ZamanArrangement(item.meetingDate).getOnlyDate()

            val dismissState = rememberDismissState(
                confirmStateChange = {
                    if (it == DismissValue.DismissedToStart) {
                        //navController.navigate(FeatureScreens.NewMeetingScreen.route)
                        CoroutineScope(Dispatchers.IO).launch{
                           // meetingViewModel.deleteMeeting(DeleteItem(item.id))
                            //AlertToDelete(DeleteItem(item.id), meetingViewModel)
                            itemToDelete = DeleteItem(item.id)
                            showDeleteDialog = true
                            //TEMPORARY SOLUTION
                            //delay(2000L)
                            //meetingViewModel.refreshMeetings(navController, BottomBarScreen.Meeting.route)
                        }
                        //delay(2000L)
                    }
                    it != DismissValue.DismissedToEnd
                }
            )
            LaunchedEffect(dismissState){
                if (!meetingViewModel.didAnimationExecute.value){
                    if (item == meetingListem.first()){
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
                    meetingViewModel.didAnimationExecute.value = true
                }
            }

            SwipeToDismiss(
                state = dismissState,
                directions = setOf(DismissDirection.EndToStart),
                dismissThresholds = { FractionalThreshold(0.3f) },
                background = { SwipeBackground(dismissState = dismissState) },
                dismissContent = {
                    MeetingCardView(
                        meetingName = item.teamName,
                        meetingType = item.meetingName,
                        meetingDate = tarih.tarih,
                        meetingClock = tarih.saat,
                        meetingContent = item.meetingContext,
                        meetingNotes = item.meetingContent,
                        navController = navController,
                        onEditClicked = { selectedMeeting = item}
                    )
                }
            )

            //Alert Dialog to delete meeting
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
                                        meetingViewModel.deleteMeeting(DeleteItem(deleteId = it.deleteId))
                                        //delay(100L)
                                        meetingViewModel.refreshMeetings(navController, BottomBarScreen.Meeting.route)
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
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // Show the update pop-up when a meeting is selected
            selectedMeeting?.let { meeting ->
                MeetingUpdatePopup(
                    meeting = meeting,
                    onDismiss = { selectedMeeting = null },
                    onUpdate = { updatedMeeting ->
                        CoroutineScope(Dispatchers.IO).launch{
                            updateViewModel.updatedMeeting(updateMeeting = updatedMeeting)
                            delay(100L)
                            meetingViewModel.refreshMeetings(navController,BottomBarScreen.Meeting.route)
                        }
                        selectedMeeting = null
                    }
                )
            }

        }

    }
}



@Composable
fun DateTimePicker(
    onValueChangeForDate: (String) -> Unit,
    onValueChangeForTime: (String) -> Unit,
    showDatePicker: Boolean,
    showTimePicker: Boolean
) {
    var calenderValue by remember{ mutableStateOf("") }
    var clockValue by remember{ mutableStateOf("") }

    val calenderState = rememberSheetState()
    val clockState = rememberSheetState()

    if(showDatePicker) {
        CalendarDialog(
            state = calenderState,
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH,
            ),
            selection = CalendarSelection.Date { date ->
                calenderValue = date.toString()
                onValueChangeForDate(calenderValue)
            })
    }
    if(showTimePicker){
        ClockDialog(
            state = clockState,
            config = ClockConfig(
                is24HourFormat = true
            ),
            selection = ClockSelection.HoursMinutes{ hours, minutes ->
                val formattedClock = String.format("%02d:%02d", hours, minutes)
                clockValue = formattedClock
                onValueChangeForTime(clockValue)
            })
    }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        TextField(
            value = calenderValue,
            onValueChange = {calenderValue = it
                //onValueChangeForDate(it)
            },
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .shadow(1.dp)
                .clickable {
                    calenderState.show()
                }
                .focusRequester(focusRequester = FocusRequester()),
            label = { Text(text = "Meeting Date") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = {
                    calenderState.show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.calendar),
                        contentDescription = null,
                        )
                }
            }
        )

        Spacer(modifier = Modifier.width(8.dp))

        TextField(
            value = clockValue,
            onValueChange = {clockValue = it
                //onValueChangeForDate(it)
            },
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .shadow(1.dp)
                .clickable {
                    clockState.show()
                }
                .focusRequester(focusRequester = FocusRequester()),
            label = { Text(text = "Meeting Time") },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                IconButton(onClick = {
                    clockState.show()
                }) {
                    Icon(
                        painter = painterResource(R.drawable.time),
                        contentDescription = null,
                    )
                }
            }
        )
    }
}