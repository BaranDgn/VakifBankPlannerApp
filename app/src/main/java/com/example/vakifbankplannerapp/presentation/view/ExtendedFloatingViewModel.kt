package com.example.vakifbankplannerapp.presentation.view

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ExtendedFloatingViewModel (): ViewModel() {

    var fabOnClick = mutableStateOf({})
    var smallFabOnClick = mutableStateOf({})

    val expandedFab = mutableStateOf(true)
}