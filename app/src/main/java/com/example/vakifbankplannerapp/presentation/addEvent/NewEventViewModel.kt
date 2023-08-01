package com.example.vakifbankplannerapp.presentation.addEvent

import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.data.model.AddEventItem
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewEventViewModel @Inject constructor(
    private val repoEvent : PlannerRepository
) : ViewModel() {
    suspend fun addEvent(addEventItem: AddEventItem){
        repoEvent.addEvent(addEventItem)
    }
}