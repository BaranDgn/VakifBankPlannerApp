package com.example.vakifbankplannerapp.presentation.meeting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.R
import com.example.vakifbankplannerapp.data.model.Teams
import com.example.vakifbankplannerapp.presentation.navigation.FeatureScreens
import com.example.vakifbankplannerapp.presentation.view.MeetingCardView
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch


@Composable
fun MeetingScreen(navController: NavController) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = "Meetings", color = Color.Black)},
                actions = {
                          IconButton(onClick = { /*TODO*/ }) {
                              Icon(imageVector = Icons.Filled.Search, contentDescription = "",
                              tint = Color.White)
                          }
                },
                backgroundColor = Color(0xffFFAE42),
                contentColor = Color.Black,
                elevation = 10.dp,
                modifier = Modifier.height(60.dp)
            )
        }
    ){
        Column(
            modifier = Modifier
                .fillMaxSize().background(Color(0xfff2f2f2))
                .padding(it)
        ) {
            //Search
            Box(modifier = Modifier
                .fillMaxWidth()){
                SearchBarForMeeting()
            }
            Text("GoTo", modifier = Modifier.clickable { navController.navigate(FeatureScreens.NewMeetingScreen.route) })

            Spacer(modifier = Modifier.height(16.dp))
           // MeetingItems("Team 1")
            //Meetin Items
            MeetingOrderByTeam(navController)

            Text("GoTo", modifier = Modifier.clickable { navController.navigate(FeatureScreens.NewMeetingScreen.route) })
        }

    }
}

var myList = listOf(
    Teams(
        "Team 1",
        "Daily meeting",
        "04.04.2023",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 4",
        "Daily meeting",
        "04.04.2023",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),
    Teams(
        "Team 2",
        "Daily meeting",
        "04.04.2023",
        "Talking about what it is done",
        "Every team's employee must attend",
    ),

)
@Composable
fun MeetingOrderByTeam(
    navController: NavController
) {
    LazyColumn(contentPadding = PaddingValues(5.dp)){
        items(myList){
            MeetingCardView(
             meetingName = it.teamName,
             meetingType =  it.meetingType,
             meetingDate =  it.meetingDate,
             meetingContent = it.meetingContent,
             meetingNotes = it.meetingNotes,
                navController = navController )
        }
    }
}

@Composable
fun SearchBarForMeeting(
    modifier: Modifier = Modifier,
    hint:String = "",
    onSearch : (String) -> Unit = {}
){
    var text by remember{
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }
    Box(modifier = modifier){
        BasicTextField(value =  text, onValueChange ={
            text = it
            onSearch(it)
        },  maxLines = 1,
            singleLine = true,
            textStyle = TextStyle.Default,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true && text.isEmpty()
                }
        )
        if(isHintDisplayed){
            Text(text = hint, color = Color.LightGray,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
fun FloatingActionButtonWithOptions() {
    var isBottomSheetOpen by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = Color.Transparent,
        sheetElevation = 0.dp,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    coroutineScope.launch { bottomSheetState.show() }
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.app_name)
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            // Your main content here
        },
        sheetContent = {
            BottomSheetOptions(
                options = listOf(
                    BottomSheetOption(Icons.Default.Favorite, "Option 1"),
                    BottomSheetOption(Icons.Default.Person, "Option 2"),
                    BottomSheetOption(Icons.Default.Settings, "Option 3")
                ),
                onItemClick = { option ->
                    // Handle the selected option here
                    coroutineScope.launch { bottomSheetState.hide() }
                }
            )
        }
    )
}

@Composable
fun BottomSheetOptions(
    options: List<BottomSheetOption>,
    onItemClick: (BottomSheetOption) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            options.forEach { option ->
                BottomSheetOptionItem(option = option, onItemClick = onItemClick)
            }
        }
    }
}

@Composable
fun BottomSheetOptionItem(
    option: BottomSheetOption,
    onItemClick: (BottomSheetOption) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onItemClick(option) }
    ) {
        Icon(
            imageVector = option.icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = option.title, fontSize = 18.sp, color = Color.Black)
    }
}
data class BottomSheetOption(val icon: ImageVector, val title: String)
@Preview
@Composable
fun FloatingActionButtonWithOptionsPreview() {
    MaterialTheme {
        FloatingActionButtonWithOptions()
    }
}
