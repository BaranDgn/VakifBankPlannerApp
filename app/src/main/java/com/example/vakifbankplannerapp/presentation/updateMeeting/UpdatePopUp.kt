package com.example.vakifbankplannerapp.presentation.updateMeeting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.Meeting
import com.example.vakifbankplannerapp.data.model.UpdateMeeting
import com.example.vakifbankplannerapp.domain.util.ZamanArrangement
import com.example.vakifbankplannerapp.ui.theme.Shapes
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockConfig
import com.maxkeppeler.sheets.clock.models.ClockSelection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MeetingUpdatePopup(
    meeting: Meeting, // The selected meeting to update
    onDismiss: () -> Unit, // Callback to dismiss the pop-up
    onUpdate: (UpdateMeeting) -> Unit, // Callback to update the meeting data,
    updateViewModel: UpdateViewModel = hiltViewModel()
) {
    // Create local mutable state for the fields that can be edited
    var updatedMeetingName by remember { mutableStateOf(meeting.meetingName) }
    var updatedMeetingContent by remember { mutableStateOf(meeting.meetingContent) }
    var updatedMeetingContext by remember { mutableStateOf(meeting.meetingContext) }

    var updatedTeamName by remember{ mutableStateOf(meeting.teamName) }
    // State to hold the selected date and time
    val tarih = ZamanArrangement(meeting.meetingDate).getOnlyDate().tarih
    val saat = ZamanArrangement(meeting.meetingTime).getOnlyDate().saat
    var selectedDate by remember { mutableStateOf(tarih) }
    var selectedTime by remember { mutableStateOf(saat) }

  //  var tarih = ZamanArrangement(selectedDate).getOnlyDate().tarih
   // var saat = ZamanArrangement(selectedTime).getOnlyDate().saat

    // State to show the Date and Time pickers
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Meeting") },
        text = {
            Column {
                // Team Name DropdownMenu
                DropDownMenuForUpdateTeams(
                    onValueChange = { updatedTeamName = it},
                    selectedTeamName = updatedTeamName
                )
                // Meeting Name TextField
                TextFieldForUpdate(
                    label = "Meeting Type",
                    value = updatedMeetingName,
                    inputType = InputTypeForUpdatingMeeting.UpdateMeetingType
                ) { updatedMeetingName = it }
                DateTimePickerForUpdate(
                    onValueChangeForDate = {selectedDate = it},
                    onValueChangeForTime = {selectedTime = it },
                    selectedDate = meeting.meetingDate,
                    selectedTime = meeting.meetingDate

                )
                // Meeting Content TextField
                TextFieldForUpdate(
                    label = "Meeting Content",
                    value = updatedMeetingContext,
                    inputType = InputTypeForUpdatingMeeting.UpdateMeetingContent,
                    multiline = true,
                    modifier = Modifier.height(90.dp)
                ) { updatedMeetingContent = it }// Adjust the height as needed

                //Meeting Notes
                TextFieldForUpdate(
                    label = "Meeting Notes",
                    value = updatedMeetingContent,
                    inputType = InputTypeForUpdatingMeeting.UpdateMeetingNotes,
                    multiline = true,
                    modifier = Modifier.height(90.dp)
                ) { updatedMeetingContext = it } // Adjust the height as needed

            }
        },
        backgroundColor = Color.LightGray,
        confirmButton = {
            TextButton(
                onClick = {
                    // Create an updated meeting object
                    val updatedMeeting = UpdateMeeting(
                        id = meeting.id,
                        teamName = updatedTeamName,
                        meetingName = updatedMeetingName,
                        meetingContent = updatedMeetingContent,
                        meetingContext = updatedMeetingContext,
                        meetingDate = createDateTimeFormatterForUpdate(selectedDate, selectedTime),
                        meetingTime = createDateTimeFormatterForUpdate(selectedDate, selectedTime)
                    )
                    // Invoke the onUpdate callback with the updated meeting
                    onUpdate(updatedMeeting)
                    // Dismiss the pop-up
                    onDismiss()
                }
            ) {
                Text("Update")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // Dismiss the pop-up without updating the meeting
                    onDismiss()
                }
            ) {
                Text("Cancel")
            }
        }
    )
    // Date picker
    if (showDatePicker) {
        CalendarDialog(
            state = rememberSheetState(),
            config = CalendarConfig(
                monthSelection = true,
                yearSelection = true,
                style = CalendarStyle.MONTH,
                //disabledDates = listOf(LocalDate.now().plusDays(7))
            ),
            selection = CalendarSelection.Date { date ->
                selectedDate = date.toString()
                showDatePicker = false
            }
        )
    }
    // Time picker
    if (showTimePicker) {
        ClockDialog(
            state = rememberSheetState(),
            config = ClockConfig(
                is24HourFormat = true
            ),
            selection = ClockSelection.HoursMinutes { hours, minutes ->
                selectedTime = String.format("%02d:%02d", hours, minutes)
                showTimePicker = false
            }
        )
    }
}


@Composable
fun TextFieldForUpdate(
    label: String,
    value: String,
    inputType: InputTypeForUpdatingMeeting,
    multiline: Boolean = false,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit

) {

    var fieldValue by remember { mutableStateOf(value) }

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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateTimePickerForUpdate(
    onValueChangeForDate: (String) -> Unit,
    onValueChangeForTime: (String) -> Unit,
    selectedDate :String,
    selectedTime : String

) {

    var calenderValue by remember{ mutableStateOf(selectedDate) }
    var clockValue by remember{ mutableStateOf(selectedTime) }

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
            val formattedClock = String.format("%02d:%02d", hours, minutes)
            clockValue = formattedClock
            onValueChangeForTime(clockValue)
        })

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        TextField(
            value = calenderValue,
            onValueChange = {calenderValue = it
                //onValueChangeForDate(it)
            },
            readOnly = true,
            modifier = Modifier

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
fun DropDownMenuForUpdateTeams(
    onValueChange: (String) -> Unit,
    selectedTeamName :String
) {

    var mExpanded by remember{ mutableStateOf(false) }
    val mTeams = listOf(
        "Platinium",
        "Rocket",
        "Storm",
        "Doremigos",
        "Mobil",
        "Orion"
    )

    var mSelectedText by remember{ mutableStateOf(selectedTeamName) }

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

@RequiresApi(Build.VERSION_CODES.O)
fun createDateTimeFormatterForUpdate(
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

sealed class InputTypeForUpdatingMeeting(
    val label: String,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {

    object UpdateMeetingType : InputTypeForUpdatingMeeting(
        label = "Meeting Type",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object UpdateMeetingContent : InputTypeForUpdatingMeeting(
        label = "Meeting Content",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object UpdateMeetingNotes : InputTypeForUpdatingMeeting(
        label = "Meeting Notes",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )

}