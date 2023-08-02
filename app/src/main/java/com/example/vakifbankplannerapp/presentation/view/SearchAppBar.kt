package com.example.vakifbankplannerapp.presentation.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun MainSearchBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    text: String,
    includeBack: Boolean = false,
    navController: NavController? = null
) {

    when(searchWidgetState){
        SearchWidgetState.CLOSED ->{
            if (navController != null) {
                DefaultAppBar (onSearchClicked = onSearchTriggered, text, includeBack, navController)
            }
            else {
                DefaultAppBar (onSearchClicked = onSearchTriggered, text, includeBack)
            }
        }
        SearchWidgetState.OPENED->{
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = { onSearchClicked(searchTextState) },

            )
        }

    }
}

@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = Color(0xffFFAE42)
    ) {

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text ,
            onValueChange = {onTextChange(it)},
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search here..",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {
                        onSearchClicked(text)
                    }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "",
                        tint = Color.White
                    )

                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if(text.isNotEmpty()){
                            onTextChange("")
                        }else{
                            onCloseClicked()
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = Color.White
                    )

                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            )
        )
    }
}

@Composable
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    text: String,
    includeBack: Boolean,
    navController: NavController? = null
) {
        TopAppBar(
            title = {
                Text(
                    text = text,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = { onSearchClicked() }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "",
                        tint = Color.White)
                }
            },
            backgroundColor = Color(0xffFFAE42),
            contentColor = Color.Black,
            elevation = 10.dp,
            modifier = Modifier.height(60.dp),
            navigationIcon = if (includeBack) {
                {
                    IconButton(onClick = { navController?.navigateUp() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "",
                            tint = Color.White)
                    }
                }
            } else null
        )
}