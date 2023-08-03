package com.example.vakifbankplannerapp.presentation.pastEvent

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

    private val _pastEventList : MutableState<Resource<Event>> = mutableStateOf(value = Resource.Loading())
    val pastEventList : State<Resource<Event>> = _pastEventList

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

    suspend fun loadPastEvents() : Resource<Event>
    {
        _pastEventList.value = repo.getPastEvent()
        return _pastEventList.value
    }

    fun searchBasedOnEventName(query : String) {
        val listOfSearch = _pastEventList.value.data!!
        _pastEventList.value = Resource.Loading()
        if (query.isEmpty()) {
            viewModelScope.launch(Dispatchers.Default) {
                _pastEventList.value = loadPastEvents()
            }
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val results = listOfSearch.filter {
                it.eventName.contains(query, ignoreCase = true)
            }
            val temp_result : Event = Event()

            temp_result.addAll(results)

            _pastEventList.value = Resource.Success(temp_result)
        }
    }
}