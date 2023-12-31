package com.example.vakifbankplannerapp.presentation.event

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.DeleteEvent
import com.example.vakifbankplannerapp.data.model.Event
import com.example.vakifbankplannerapp.data.model.SearchEvent
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.presentation.bottomBar.BottomBarScreen
import com.example.vakifbankplannerapp.presentation.view.SearchWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    suspend fun searchEvents(searchText: SearchEvent) : Resource<Event>
    {
        return repoEvent.searchEvent(searchText)
    }


    suspend fun deleteSelectedEvent(deleteEvent: DeleteEvent){
        repoEvent.deleteEvent(deleteEvent)
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

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

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshEvents(navController: NavController, route :String){
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000L)
            navController.navigate(route)
            _isLoading.value = false
        }
    }
}