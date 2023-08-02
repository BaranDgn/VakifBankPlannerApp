package com.example.vakifbankplannerapp.presentation.birthday

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.vakifbankplannerapp.data.model.Birthday
import com.example.vakifbankplannerapp.data.repository.PlannerRepository
import com.example.vakifbankplannerapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BirthdayViewModel@Inject constructor (
    private val repo : PlannerRepository
) : ViewModel() {
    suspend fun loadBirthdays() : Resource<Birthday> {
        return repo.getBirthday()
    }


}