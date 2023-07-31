package com.example.vakifbankplannerapp.domain.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vakifbankplannerapp.data.model.Zaman
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

open class ZamanArrangement(var fullTarih: String) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun getOnlyDate() : Zaman {
        val meetingDateTime = fullTarih

        // Parse the string to a LocalDateTime object
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dateTime = LocalDateTime.parse(meetingDateTime, formatter)

        // Extract date in yyyy-MM-dd format
        val date = dateTime.toLocalDate()

        // Extract time in HH:mm format
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val time = dateTime.format(timeFormatter)

        return Zaman( tarih = date.toString(), saat = time.toString())

    }
}