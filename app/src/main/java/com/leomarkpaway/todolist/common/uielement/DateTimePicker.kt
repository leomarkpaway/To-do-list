package com.leomarkpaway.todolist.common.uielement

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.widget.EditText
import com.leomarkpaway.todolist.R
import com.leomarkpaway.todolist.common.enum.Pattern
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DateTimePicker(private val context: Context) {
    private val calendar: Calendar = Calendar.getInstance()
    private var millis: Long = 0

    fun showDateTimePicker(view: View) {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context, { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    context, { _, hourOfDay, minute ->
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        calendar.set(Calendar.MINUTE, minute)
                        millis = calendar.timeInMillis

                        view as EditText
                        val dayName = getConvertedMillis(Pattern.DAY_NAME.id)
                        val time = getConvertedMillis(Pattern.TIME.id)
                        val date =  getConvertedMillis(Pattern.DATE.id)
                        val formattedTimeDate = context.getString(R.string.input_holder_time_and_date, dayName,time, date)
                        view.setText(formattedTimeDate)
                    },
                    hour,
                    minute,
                    true
                )

                timePickerDialog.show()
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    fun getCalendar() = calendar

    fun getMillis() = millis

    fun getConvertedMillis(pattern: String): String {
        calendar.timeInMillis = millis
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(calendar.time)
    }

    companion object {
        fun newInstance(context: Context): DateTimePicker {
            return DateTimePicker(context)
        }
    }
}