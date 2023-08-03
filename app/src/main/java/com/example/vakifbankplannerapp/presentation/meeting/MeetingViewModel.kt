package com.example.vakifbankplannerapp.presentation.meeting


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.vakifbankplannerapp.data.model.DeleteItem
import com.example.vakifbankplannerapp.data.model.Meeting
import com.example.vakifbankplannerapp.data.model.MeetingItem
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.domain.util.Resource
import com.example.vakifbankplannerapp.presentation.bottomBar.BottomBarScreen
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

    val didAnimationExecute = mutableStateOf(false)

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

    //Fetch Meetings
    //suspend fun loadMeetings() : Resource<MeetingItem>{
     //   return repo.getMeeting()
    //}

    //Delete Meeting
    suspend fun deleteMeeting(deleteItem: DeleteItem){
        repo.deleteMeetingRepo(deleteMeeting = deleteItem)
    }



    //Search
    private var initialMeetingList = listOf<Meeting>()
    var filteredMeetingList = listOf<Meeting>()

    // Define a function to update both lists
    private fun updateMeetingList(meetings: List<Meeting>) {
        initialMeetingList = meetings
        filteredMeetingList = meetings
    }

    // Fetch Meetings and update the lists
    suspend fun loadMeetings(): Resource<MeetingItem> {
        val result = repo.getMeeting()
        if (result is Resource.Success) {
            updateMeetingList(result.data ?: emptyList())
        }
        return result
    }


    //----

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

    private var isSearchStarting = true
    //private var initialMeetingList = listOf<Meeting>()
    fun searchBasedOnTeamName() {
        val query = searchTextState.value.trim()

        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                filteredMeetingList = initialMeetingList
                return@launch
            }

            filteredMeetingList = initialMeetingList.filter {
                it.teamName.contains(query, ignoreCase = true)
            }
        }
    }


    //Refresh
    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshMeetings(navController: NavController, root: String){
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000L)
            navController.navigate(root)
            _isLoading.value = false
        }
    }
}


/*
    fun loadMeetings2(){
        viewModelScope.launch {

            val result = repo.getMeeting()
            when(result){
                is Resource.Success->{
                    val meetingItems = result.data!!.mapIndexed { index, meetings ->
                        Meeting(
                             id= meetings.id,
                             isMeeting = meetings.isMeeting,
                            meetingContent = meetings.meetingContent,
                            meetingContext = meetings.meetingContext,
                            meetingDate = meetings.meetingDate,
                            meetingName = meetings.meetingName,
                            meetingTime = meetings.meetingTime,
                            teamName = meetings.teamName
                        )
                    }
                    errorMessage.value = ""
                    meetingList.value += meetingItems

                }
                is Resource.Error->{
                    errorMessage.value = repo.getMeeting().message!!.toString()
                }
                else-> Unit
            }
        }
    }
*/