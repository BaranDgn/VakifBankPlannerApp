package com.example.vakifbankplannerapp.pastEvent

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.data.model.EventItem
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PastEventViewModel @Inject constructor(
    private var repo : PlannerRepository
) : ViewModel(){
    suspend fun loadPastEvents() : Resource<Event>
    {
        return repo.getPastEvent()
    }

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