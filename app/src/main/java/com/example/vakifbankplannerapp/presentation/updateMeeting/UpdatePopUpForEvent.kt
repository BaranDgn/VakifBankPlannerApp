package com.example.vakifbankplannerapp.presentation.updateMeeting

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.data.model.EventItem
import com.example.vakifbankplannerapp.data.model.UpdateEvent
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdatePopUpForEvent(
    event : EventItem,
    onDismiss :() -> Unit,
    onUpdate : (UpdateEvent) -> Unit,
    updateViewModel: UpdateViewModel = hiltViewModel()
) {

    var updatedEventName by remember { mutableStateOf(event.eventName) }
    var updateEventType by remember { mutableStateOf(event.eventType) }
    var updatedEventNotes by remember { mutableStateOf(event.eventNotes) }

    val tarih = ZamanArrangement(event.eventDateTime).getOnlyDate().tarih
    val saat = ZamanArrangement(event.eventDateTime).getOnlyDate().saat
    var selectedDate by remember { mutableStateOf(tarih) }
    var selectedTime by remember { mutableStateOf(saat) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Update Event") },
            text = {
                Column {

                    // Event Name TextField
                    TextFieldForUpdateEvent(
                        label = "Event Name",
                        value = updatedEventName,
                        inputType = InputTypeForUpdatingEvent.UpdateEventName
                    ) { updatedEventName = it }

                    DateTimePickerForUpdateEvent(
                        onValueChangeForDate = { selectedDate = it },
                        onValueChangeForTime = { selectedTime = it },
                        selectedDate = event.eventDateTime,
                        selectedTime = event.eventDateTime

                    )
                    // Meeting Content TextField
                    TextFieldForUpdateEvent(
                        label = "Event Type",
                        value = updateEventType,
                        inputType = InputTypeForUpdatingEvent.UpdateEventType,
                    ) { updateEventType = it }// Adjust the height as needed

                    //Meeting Notes
                    TextFieldForUpdateEvent(
                        label = "Meeting Notes",
                        value = updatedEventNotes,
                        inputType = InputTypeForUpdatingEvent.UpdateEventNotes,
                        multiline = true,
                        modifier = Modifier.height(90.dp)
                    ) { updatedEventNotes = it } // Adjust the height as needed

                }
            },
            backgroundColor = Color.LightGray,
            confirmButton = {
                TextButton(
                    onClick = {
                        // Create an updated meeting object
                        val updatedEvent = UpdateEvent(
                            id = event.id,
                            eventName = updatedEventName,
                            eventType = updateEventType,
                            eventNotes = updatedEventNotes,
                            eventDateTime = createDateTimeFormatterForUpdate(
                                selectedDate,
                                selectedTime
                            ),

                            )
                        // Invoke the onUpdate callback with the updated meeting
                        onUpdate(updatedEvent)
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
fun TextFieldForUpdateEvent(
    label: String,
    value: String,
    inputType: InputTypeForUpdatingEvent,
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
fun DateTimePickerForUpdateEvent(
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


sealed class InputTypeForUpdatingEvent(
    val label: String,
    val keyboardOptions: KeyboardOptions,
    val visualTransformation: VisualTransformation
) {

    object UpdateEventName : InputTypeForUpdatingEvent(
        label = "Event Name",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object UpdateEventType : InputTypeForUpdatingEvent(
        label = "Event Type",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )
    object UpdateEventNotes : InputTypeForUpdatingEvent(
        label = "Event Notes",
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
        visualTransformation = VisualTransformation.None
    )

}