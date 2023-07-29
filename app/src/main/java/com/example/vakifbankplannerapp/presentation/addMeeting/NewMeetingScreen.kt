package com.example.vakifbankplannerapp.presentation.addEvent


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.AddMeetingItem
import com.example.vakifbankplannerapp.presentation.addMeeting.NewMeetingViewModel
import com.example.vakifbankplannerapp.presentation.navigation.AuthScreen
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.navigation.Graph
import com.example.vakifbankplannerapp.ui.theme.Shapes
import com.maxkeppeker.sheets.core.models.base.SheetState
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
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NewMeetingScreen(
    navController: NavHostController,
    newMeetingViewModel: NewMeetingViewModel = hiltViewModel()
) {

    val meetingDataList : MutableList<AddMeetingItem> = remember { mutableStateListOf<AddMeetingItem>() }

    var teamNameValue by remember { mutableStateOf("") }
    var meetingTypeValue by remember { mutableStateOf("") }
    var meetingDateValue by remember { mutableStateOf("") }
    var meetingTimeValue by remember { mutableStateOf("") }
    var meetingContentValue by remember { mutableStateOf("") }
    var meetingNotesValue by remember { mutableStateOf("") }

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

                            Text(text = "Add New Meeting", fontSize = 20.sp, fontWeight = FontWeight.SemiBold,  maxLines= 1, textAlign = TextAlign.Center)
                        }
                    }

                }
            }
        }
    ){

        Column(horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xfff2f2f2))
                .padding(it)
                .padding(8.dp)
        ) {

            //Team Name
            DropDownMenuForTeams(onValueChange = {teamNameValue = it})

            //Meeting Type
            TextFieldForMeeting(
                label = "Meeting Type",
                inputType = InputTypeForAddingMeeting.MeetingType,
                onValueChange =  { meetingTypeValue = it })

            //Meeting Data and Time
            DateTimePicker(
                onValueChangeForDate = {meetingDateValue = it},
                onValueChangeForTime = {meetingTimeValue = it}
            )

            //Meeting Content
          TextFieldForMeeting(
                label = "Meeting Content",
                inputType = InputTypeForAddingMeeting.MeetingContent,
                multiline = true,
                modifier = Modifier.height(120.dp),
              onValueChange = { meetingContentValue = it  } // Adjust the height as needed
            )
            //Meeting Notes
            TextFieldForMeeting(
                label = "Meeting Notes",
                inputType = InputTypeForAddingMeeting.MeetingNotes,
                multiline = true,
                modifier = Modifier.height(120.dp),
                onValueChange = { meetingNotesValue = it  } // Adjust the height as needed
            )
            Spacer(modifier = Modifier.height(32.dp))

            //Save Button
            MeetingButton(navController){

                val newMeetingData = AddMeetingItem(
                    teamName = teamNameValue,
                    meetingName = meetingTypeValue,
                    meetingDate = createDateTimeFormatter(meetingDateValue, meetingTimeValue),
                    meetingTime =  createDateTimeFormatter(meetingDateValue, meetingTimeValue),
                    meetingContext = meetingContentValue,
                    meetingContent = meetingNotesValue
                )

                // Add the newMeetingData object to the list
                meetingDataList.add(newMeetingData)

                CoroutineScope(Dispatchers.IO).launch{

                    newMeetingViewModel.sendMeetingItems(newMeetingData)
                }

                // Clear the text field values
                teamNameValue = ""
                meetingTypeValue = ""
                meetingDateValue = ""
                meetingTimeValue = ""
                meetingContentValue = ""
                meetingNotesValue = ""

                navController.navigate(Graph.HOME){
                    popUpTo(FeatureScreens.NewMeetingScreen.route){inclusive = true}
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
fun createDateTimeFormatter(
    meetingDateValue: String,
    meetingTimeValue: String
): String {

    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val date: LocalDate = LocalDate.parse(meetingDateValue, dateFormatter)
    val time: LocalTime = LocalTime.parse(meetingTimeValue, timeFormatter)

    val dateTime: LocalDateTime = date.atTime(time)

    val outputFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")

    return dateTime.format(outputFormatter)
}

@Composable
fun MeetingButton(
    navController: NavController,
    onClick: () -> Unit
) {
    Button(
        onClick = { onClick() },
        modifier = Modifier//.size(width = 60.dp, height = 30.dp)
            .size(width = 170.dp, height = 60.dp)
            .shadow(2.dp),
        colors = ButtonDefaults.buttonColors(
            Color(0xFF4E4F50),
            contentColor = Color(0xFFE2DED0)
        )
    ) {
        Text(text = "SAVE MEETING", modifier = Modifier.padding(vertical = 8.dp))
    }

}
@Composable
fun TextFieldForMeeting(
    label : String,
    inputType : InputTypeForAddingMeeting,
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


sealed class InputTypeForAddingMeeting(
    val label: String,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {
    object TeamName : InputTypeForAddingMeeting(
        label = "Team Name",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None),
        visualTransformation = VisualTransformation.None
    )

    object MeetingType : InputTypeForAddingMeeting(
        label = "Meeting Type",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object MeetingDate : InputTypeForAddingMeeting(
        label = "Meeting Date",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object MeetingTime : InputTypeForAddingMeeting(
        label = "Meeting Time",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object MeetingContent : InputTypeForAddingMeeting(
        label = "Meeting Content",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object MeetingNotes : InputTypeForAddingMeeting(
        label = "Meeting Notes",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerScreen() {
    //to trigger the dialog
    val calenderState = rememberSheetState()

    CalendarDialog(
        state = calenderState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            //disabledDates = listOf(LocalDate.now().plusDays(7))
        ),
        selection = CalendarSelection.Date{date ->
         //   calenderState = date.toString()
        })

    Box(){
        calenderState.show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClockTimePicker(clockState : SheetState) {
    //val clockState = rememberSheetState()
    ClockDialog(
        state = clockState,
        config = ClockConfig(
            is24HourFormat = true
        ),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
            print("$hours : $minutes")
        })
        clockState.show()
}

@Composable
fun DateTimePicker(
    onValueChangeForDate: (String) -> Unit,
    onValueChangeForTime: (String) -> Unit
) {
    var calenderValue by remember{ mutableStateOf("") }
    var clockValue by remember{ mutableStateOf("") }

    val calenderState = rememberSheetState()
    val clockState = rememberSheetState()

    CalendarDialog(
        state = calenderState,
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true,
            style = CalendarStyle.MONTH,
            //disabledDates = listOf(LocalDate.now().plusDays(7))
        ),
        selection = CalendarSelection.Date{date ->
            calenderValue = date.toString()
            onValueChangeForDate(calenderValue)
        })
    ClockDialog(
        state = clockState,
        config = ClockConfig(
            is24HourFormat = true
        ),
        selection = ClockSelection.HoursMinutes{ hours, minutes ->
            clockValue = "$hours:$minutes"
            onValueChangeForTime(clockValue)
        })

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
                        painter = androidx.compose.ui.res.painterResource(R.drawable.calendar),
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

@Composable
fun DropDownMenuForTeams(
    onValueChange: (String) -> Unit
) {

    var mExpanded by remember{ mutableStateOf(false) }
    val mTeams = listOf(
        "Team 1",
        "Team 2",
        "Team 3",
        "Team 4",
        "Team 5",
        "Team 6"
    )

    var mSelectedText by remember{ mutableStateOf("") }

    var mTextFieldSize by remember{ mutableStateOf(Size.Zero) }

    val icon = if(mExpanded) Icons.Default.KeyboardArrowUp
    else Icons.Default.KeyboardArrowDown

    Column() {
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = {mSelectedText = it
                //onValueChange(it)
                            },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text(text = "Team")},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )

        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier.width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
        ) {
            mTeams.forEach {label ->
                DropdownMenuItem(onClick = {
                    mSelectedText = label
                    onValueChange(label)
                    mExpanded = false

                }) {
                    Text(text = label)
                }
            }
        }

    }


}


