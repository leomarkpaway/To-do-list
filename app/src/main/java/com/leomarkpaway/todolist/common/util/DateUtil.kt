package com.leomarkpaway.todolist.common.util

import android.os.Build
import androidx.annotation.RequiresApi
import com.leomarkpaway.todolist.common.enum.Pattern
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@RequiresApi(Build.VERSION_CODES.O)
fun getCurrentWeek(): ArrayList<Pair<String, String>> {
    val week = ArrayList<Pair<String, String>>()
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern(Pattern.DATE.id)
    val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    val endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
    var currentDate = startOfWeek

    while (currentDate.isBefore(endOfWeek) || currentDate.isEqual(endOfWeek)) {
        week.add(Pair(currentDate.format(formatter), currentDate.dayOfWeek.toString()))
        currentDate = currentDate.plusDays(1)
    }
    return week
}

fun String.getDayDate() = this.split("/")[1]

fun String.toShortDayName(): String {
    val shortDayName = when (this) {
        "MONDAY" -> { "Mon" }
        "TUESDAY" -> { "Tues" }
        "WEDNESDAY" -> { "Wed" }
        "THURSDAY" -> { "Thur" }
        "FRIDAY" -> { "Fri" }
        "SATURDAY" -> { "Sat" }
        "SUNDAY" -> { "Sun" }
        else -> ""
    }
    return shortDayName
}
