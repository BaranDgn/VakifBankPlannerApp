package com.example.vakifbankplannerapp.presentation.addMeeting

import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.data.model.AddMeetingItem
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NewMeetingViewModel@Inject constructor(
    private val repoForAddMeeting : PlannerRepository
)  : ViewModel(){
    suspend fun sendMeetingItems(addMeetingItem: AddMeetingItem){
        repoForAddMeeting.addMeetingRepo(addMeetingItem)
    }
}