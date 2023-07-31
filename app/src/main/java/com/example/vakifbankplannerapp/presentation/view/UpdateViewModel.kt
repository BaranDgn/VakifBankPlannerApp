package com.example.vakifbankplannerapp.presentation.view

import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.data.model.UpdateMeeting
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel@Inject constructor(
    private var repoUpdate : PlannerRepository
) : ViewModel() {

    suspend fun updatedMeeting(updateMeeting: UpdateMeeting){
        repoUpdate.updateMeetingRepo(updateMeeting = updateMeeting)
    }
}