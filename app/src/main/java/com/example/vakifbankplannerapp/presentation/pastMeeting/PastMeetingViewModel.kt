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

    private val _pastMeetingList : MutableState<Resource<MeetingItem>> = mutableStateOf(value = Resource.Loading())
    val pastMeetingList : State<Resource<MeetingItem>> = _pastMeetingList

    fun updateSearchWidgetState(newValue: SearchWidgetState){
        _searchWidgetState.value = newValue
    }

    fun updateSearchTextState(newValue: String){
        _searchTextState.value = newValue
    }

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    suspend fun loadPastMeetings() : Resource<MeetingItem> {
        _pastMeetingList.value = repo.getPastMeeting()
        return _pastMeetingList.value
    }

    fun searchBasedOnTeamName(query : String) {
        val listOfSearch = _pastMeetingList.value.data!!
        _pastMeetingList.value = Resource.Loading()
        if (query.isEmpty()) {
            viewModelScope.launch(Dispatchers.Default) {
                _pastMeetingList.value = loadPastMeetings()
            }
            return
        }
        viewModelScope.launch(Dispatchers.Default) {
            val results = listOfSearch.filter {
                it.teamName.contains(query.trim(), ignoreCase = true)
            }
            val temp_result : MeetingItem = MeetingItem()

            temp_result.addAll(results)

            _pastMeetingList.value = Resource.Success(temp_result)
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