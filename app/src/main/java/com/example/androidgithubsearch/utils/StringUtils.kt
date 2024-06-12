package com.example.androidgithubsearch.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.dateStringToDate(): Date {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    return formatter.parse(this) ?: Date()
}