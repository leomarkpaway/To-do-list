package com.leomarkpaway.todolist.common.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun Long.convertMillis(calendar: Calendar, pattern: String): String {
    calendar.timeInMillis = this
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(calendar.time)
}