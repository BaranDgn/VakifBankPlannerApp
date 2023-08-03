package com.example.vakifbankplannerapp.domain.util

object AdminControl {
/*
    companion object{
        @Volatile
        private var instance : AdminControl ?= null

        fun getAdminControlValue() : AdminControl =
            instance ?: synchronized(this){
                instance ?: AdminControl().also { instance = it }
            }
    }

    var adminControlValue : Boolean = false
    */

    var adminControl : Boolean = false
        private set

    fun setAdminControl(value : Boolean){
        adminControl = value
    }
}