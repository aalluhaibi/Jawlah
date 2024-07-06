package com.example.jawlah.presentation.util

import java.text.SimpleDateFormat
import java.util.*

fun convertLongToDateString(timeInMillis: Long): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date(timeInMillis))
}