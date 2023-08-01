package com.example.vakifbankplannerapp.presentation.pastMeeting

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vakifbankplannerapp.data.model.Meeting
import com.example.vakifbankplannerapp.data.model.MeetingItem
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
class PastMeetingViewModel @Inject constructor(
    private var repo : PlannerRepository
) : ViewModel(){
    private val _searchWidgetState : MutableState<SearchWidgetState> = mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState : State<SearchWidgetState> =_searchWidgetState

    private val _searchTextState : MutableState<String> = mutableStateOf(value ="")
    val searchTextState : State<String> = _searchTextState

    fun updateSearchWidgetState(newValue: SearchWidgetState){
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String){
        _searchTextState.value = newValue
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    var pastMeetingList = mutableStateOf<List<Meeting>>(listOf())

    suspend fun loadPastMeetings() : Resource<MeetingItem> {
        return repo.getPastMeeting()
    }

    private var isSearchStarting = true
    private var initialMeetingList = listOf<Meeting>()
    fun searchBasedOnTeamName(query : String){
        val listOfSearch = if(isSearchStarting){
            pastMeetingList.value
        }else{
            initialMeetingList
        }
        viewModelScope.launch(Dispatchers.Default){
            if(query.isEmpty()){
                pastMeetingList.value = initialMeetingList
                isSearchStarting = true
                return@launch
            }
            val results = listOfSearch.filter {
                it.teamName.contains(query.trim(), ignoreCase = true)
            }

            if(isSearchStarting){
                initialMeetingList = pastMeetingList.value
                isSearchStarting = false
            }
            pastMeetingList.value = results
        }
    }

    fun loadStuff(){
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000L)
            loadPastMeetings()
            _isLoading.value = false
        }
    }
}