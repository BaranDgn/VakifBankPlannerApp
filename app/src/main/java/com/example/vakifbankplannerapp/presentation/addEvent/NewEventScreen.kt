package com.example.vakifbankplannerapp.presentation.addEvent

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.AddEventItem
import com.example.vakifbankplannerapp.data.model.AddMeetingItem
import com.example.vakifbankplannerapp.presentation.bottomBar.BottomBarScreen
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.navigation.Graph
import com.example.vakifbankplannerapp.presentation.navigation.HomeNavGraph
import com.example.vakifbankplannerapp.ui.theme.Shapes
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewEventScreen(
    navController: NavHostController,
    eventViewModel: NewEventViewModel = hiltViewModel()
) {
    val eventDataList : MutableList<AddEventItem> = remember { mutableStateListOf<AddEventItem>() }

    val context = LocalContext.current

    var teamNameValue by remember { mutableStateOf("") }
    var eventTypeValue by remember { mutableStateOf("") }
    var eventDateValue by remember { mutableStateOf("") }
    var eventClockValue by remember { mutableStateOf("") }
    var eventNotesValue by remember { mutableStateOf("") }

    fun isEventDataValid(): Boolean {
        return teamNameValue.isNotBlank()
                && eventTypeValue.isNotBlank()
                && eventDateValue.isNotBlank()
                && eventClockValue.isNotBlank()
                && eventNotesValue.isNotBlank()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                // title = {Text(text = "Add New Meeting", color = Color(0xff2F5061))},

                backgroundColor = Color(0xffFFAE42),
                contentColor = Color.Black,
                elevation = 10.dp,
                modifier = Modifier.height(60.dp)
            ){
                Box(modifier = Modifier.fillMaxWidth()) {

                    //Navigation Content
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable {navController.navigate(Graph.HOME) }

                        ){
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = "",
                            )
                            //Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Back", fontSize = 18.sp)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxSize()
                            .padding(68.dp, 0.dp, 0.dp, 0.dp)){

                            Text(text = "Add New Event", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,  maxLines= 1, textAlign = TextAlign.Center)
                        }
                    }

                }
            }
        }
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff2f2f2))
                .padding(it)
                .padding(8.dp),
        ) {

            //  TextFieldForMeeting(label = "Team Name", inputType = InputTypeForAddingMeeting.TeamName)
            TextFieldForEvent(
                label = "Event Name",
                inputType = InputTypeForAddingEvents.EventName,
                onValueChange =  { teamNameValue = it } )

            TextFieldForEvent(
                label = "Event Type",
                inputType = InputTypeForAddingEvents.EventType,
                onValueChange =  { eventTypeValue = it } )

            DateTimePickerForEvent(
                onValueChangeForDate = {eventDateValue = it},
                onValueChangeForClock = {eventClockValue = it},
            )

            TextFieldForEvent(
                label = "Meeting Notes",
                inputType = InputTypeForAddingEvents.EventNotes,
                multiline = true,
                modifier = Modifier.height(120.dp), // Adjust the height as needed
                onValueChange = {eventNotesValue = it}
            )

            Spacer(modifier = Modifier.height(32.dp))

            EventButton(
                navController,
                isEventDataValid()
            ){
                if(isEventDataValid()){
                    val newEventList = AddEventItem(
                        eventName = teamNameValue,
                        eventType = eventTypeValue,
                        eventDateTime = createDateTimeFormatterForEvent(eventDateValue, eventClockValue),
                        eventNotes = eventNotesValue,
                    )
                    eventDataList.add(newEventList)

                    CoroutineScope(Dispatchers.IO).launch{
                        eventViewModel.addEvent(newEventList)
                    }

                    teamNameValue = ""
                    eventTypeValue = ""
                    eventDateValue = ""
                    eventClockValue = ""
                    eventNotesValue = ""

                    navController.navigate(BottomBarScreen.Event.route){
                        popUpTo(FeatureScreens.NewEventScreen.route){inclusive = true}
                    }
                }else{
                    Toast.makeText(context, "You need to make sure all the fields are filled", Toast.LENGTH_LONG).show()
                }
            }
        }

    }
}
@Composable
fun TextFieldForEvent(
    label : String,
    inputType : InputTypeForAddingEvents,
    multiline: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange : (String) -> Unit
) {

    var fieldValue by remember { mutableStateOf("") }

    TextField(
        value = fieldValue,
        onValueChange = {fieldValue = it
                        onValueChange(it)},
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(1.dp)
            .focusRequester(focusRequester = FocusRequester()),
        // leadingIcon = { Icon(imageVector = InputType.UserName.icon, contentDescription = null) },
        label = { Text(text = label) },
        //shape = Shapes.small,
        shape = Shapes.medium,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent),
        singleLine = !multiline,
        keyboardOptions = inputType.keyboardOptions,
        visualTransformation = inputType.visualTransformation,
        //shape = RoundedCornerShape(12.dp)
    )
}
@Composable
fun EventButton(
    navController: NavController,
    eventDataIsValid :Boolean,
    onSaveClick :() -> Unit
) {
    Button(
        onClick = {
            if(eventDataIsValid){
                onSaveClick()
            }

            //navController.navigate(BottomBarScreen.Event.route){
             //   popUpTo(FeatureScreens.NewEventScreen.route){inclusive = true}
           // }
        },
        modifier = Modifier//.size(width = 60.dp, height = 30.dp)
            .size(width = 170.dp, height = 60.dp)
            .shadow(2.dp),
        colors = ButtonDefaults.buttonColors(
            Color(0xFF4E4F50),
            contentColor = Color(0xFFE2DED0)
        ),
        enabled = eventDataIsValid
    ) {
        Text(text = "SAVE EVENT", modifier = Modifier.padding(vertical = 8.dp))
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerForEvent(
    onValueChangeForDate: (String) -> Unit,
    onValueChangeForClock: (String) -> Unit,

    ) {
    var eventCalenderValue by remember{ mutableStateOf("") }
    var eventClockValue by remember{ mutableStateOf("") }

    val eventCalenderState = rememberSheetState()
    val eventClockState = rememberSheetState()

    CalendarDialog(
        state = eventCalenderState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            //disabledDates = listOf(LocalDate.now().plusDays(7))
        ),
        selection = CalendarSelection.Date{ date ->
            eventCalenderValue = date.toString()
            onValueChangeForDate(eventCalenderValue)
        })
    ClockDialog(
        state = eventClockState,
        config = ClockConfig(
            is24HourFormat = true
        ),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
            val formattedClock = String.format("%02d:%02d", hours, minutes)
            eventClockValue = formattedClock
            onValueChangeForClock(eventClockValue)
        })

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        TextField(
            value = eventCalenderValue,
            onValueChange = {eventCalenderValue = it},
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .shadow(1.dp)

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
                    eventCalenderState.show()
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
            value = eventClockValue,
            onValueChange = {eventClockValue = it},
            readOnly = true,
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
                .shadow(1.dp)

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
                    eventClockState.show()
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
sealed class InputTypeForAddingEvents(
    val label: String,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object EventName : InputTypeForAddingEvents(
        label = "Event Name",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
        visualTransformation = VisualTransformation.None
    )

    object EventType : InputTypeForAddingEvents(
        label = "Event Type",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object EventDate : InputTypeForAddingEvents(
        label = "Event Date",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object EventTime : InputTypeForAddingEvents(
        label = "Event Time",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object EventNotes : InputTypeForAddingEvents(
        label = "Meeting Notes",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object Notes : InputTypeForAddingEvents(
        label = "Meeting Notes",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )

}


@RequiresApi(Build.VERSION_CODES.O)
fun createDateTimeFormatterForEvent(
    meetingDateValue: String,
    meetingTimeValue: String,

): String {

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val date: LocalDate = LocalDate.parse(meetingDateValue, dateFormatter)
    val time: LocalTime = LocalTime.parse(meetingTimeValue, timeFormatter)

    val dateTime: LocalDateTime = date.atTime(time)

    val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    return dateTime.format(outputFormatter)
}