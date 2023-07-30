package com.example.vakifbankplannerapp.presentation.meeting


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vakifbankplannerapp.data.model.DeleteItem
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
class MeetingViewModel@Inject constructor(
    private var repo : PlannerRepository
) : ViewModel() {

    private val _searchWidgetState : MutableState<SearchWidgetState> = mutableStateOf(value = SearchWidgetState.CLOSED)
    val searchWidgetState : State<SearchWidgetState>  =_searchWidgetState

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

    var meetingList = mutableStateOf<List<Meeting>>(listOf())
    //var errorMessage = mutableStateOf("")
    //var isLoadingForMeeting = mutableStateOf(false)


    init{
       // laodMeetings2()
    }

    suspend fun loadMeetings() : Resource<MeetingItem>{
        return repo.getMeeting()
    }

    suspend fun deleteMeeting(deleteItem: DeleteItem){
        repo.deleteMeetingRepo(deleteMeeting = deleteItem)
    }
    private var isSearchStarting = true
    private var initialMeetingList = listOf<Meeting>()
    fun searchBasedOnTeamName(query : String){
        val listOfSearch = if(isSearchStarting){
            meetingList.value
        }else{
            initialMeetingList
        }
        viewModelScope.launch(Dispatchers.Default){
            if(query.isEmpty()){
                meetingList.value = initialMeetingList
                isSearchStarting = true
                return@launch
            }
            val results = listOfSearch.filter {
                it.teamName.contains(query.trim(), ignoreCase = true)
            }

            if(isSearchStarting){
                initialMeetingList = meetingList.value
                isSearchStarting = false
            }
            meetingList.value = results
        }
    }


    /*
    fun laodMeetings2(){
        viewModelScope.launch {
            isLoadingForMeeting.value = true
            val result = repo.getMeeting()
            when(result){
                is Resource.Success->{
                    val meetingItems = result.data!!.mapIndexed { index, meetings ->
                        Meeting(
                            meetings.teamName,
                            meetings.meetingName,
                            meetings.meetingDate,
                            meetings.meetingTime,
                            meetings.meetingContent,
                            meetings.meetingContext
                        )
                    }
                    errorMessage.value = ""
                    isLoadingForMeeting.value = false
                    meetingList.value += meetingItems

                }
                is Resource.Error->{
                    errorMessage.value = repo.getMeeting().message!!.toString()
                    isLoadingForMeeting.value = false
                }
                else-> Unit
            }
        }
    }
    */

    fun loadStuff(){
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000L)
            loadMeetings()
            _isLoading.value = false
        }
    }




}