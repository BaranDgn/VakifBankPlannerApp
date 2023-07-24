package com.example.vakifbankplannerapp.presentation.event

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState

class EventViewModel() : ViewModel() {

    private val _searchWidgetStateForEvent : MutableState<SearchWidgetState> = mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetStateForEvent : State<SearchWidgetState> =_searchWidgetStateForEvent

    private val _searchTextStateForEvent : MutableState<String> = mutableStateOf(value ="")
    val searchTextStateForEvent : State<String> = _searchTextStateForEvent

    fun updateSearchWidgetStateForEvent(newValue: SearchWidgetState){
        _searchWidgetStateForEvent.value = newValue
    }

    fun updateSearchTextStateForEvent(newValue: String){
        _searchTextStateForEvent.value = newValue
    }
}