package com.ssafy.ui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

//fun formatDate(date: Date): String {
//    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
//    return dateFormat.format(date)
//}

fun Date.formatDate(): String {
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    return dateFormat.format(this)
}