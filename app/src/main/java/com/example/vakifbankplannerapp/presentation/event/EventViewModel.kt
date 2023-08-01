package com.example.vakifbankplannerapp.presentation.event

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.data.model.AddEventItem
import com.example.vakifbankplannerapp.data.model.DeleteEvent
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class EventViewModel@Inject constructor(
    private val repoEvent : PlannerRepository
) : ViewModel() {

    val didAnimationExecute = mutableStateOf(false)

    suspend fun loadEvents() : Resource<Event>
    {
        return repoEvent.getEvent()
    }


    suspend fun deleteSelectedEvent(deleteEvent: DeleteEvent){
        repoEvent.deleteEvent(deleteEvent)
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