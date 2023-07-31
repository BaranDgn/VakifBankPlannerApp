package com.example.vakifbankplannerapp.presentation.birthday

import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel@Inject constructor (
    private val repo : PlannerRepository
) : ViewModel() {

}