package com.example.dailywork.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarView(
    selectedDate: Calendar,  // The currently selected date from parent
    onDateSelected: (Calendar) -> Unit  // Updated to pass the Calendar object
) {
    val today = Calendar.getInstance()
    var currentMonth by remember { mutableStateOf(today.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(today.get(Calendar.YEAR)) }

    // Get list of days in the current month
    val daysInMonth = getDaysInMonth(currentMonth, currentYear)

    // Column layout to arrange the components
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month and Year Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "<",
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    if (currentMonth == 0) {
                        currentMonth = 11
                        currentYear -= 1
                    } else {
                        currentMonth -= 1
                    }
                }
            )

            Text(
                text = "${getMonthName(currentMonth)} $currentYear",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )

            Text(
                text = ">",
                fontSize = 24.sp,
                modifier = Modifier.clickable {
                    if (currentMonth == 11) {
                        currentMonth = 0
                        currentYear += 1
                    } else {
                        currentMonth += 1
                    }
                }
            )
        }

        // Grid for days of the month
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(daysInMonth) { day ->
                // Convert day, month, and year to Calendar instance
                val dayCalendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, currentYear)
                    set(Calendar.MONTH, currentMonth)
                    set(Calendar.DAY_OF_MONTH, day.toInt())
                }

                DayItem(
                    day = day,
                    isSelected = dayCalendar.timeInMillis == selectedDate.timeInMillis,  // Compare with selected date
                    onDayClick = { selectedDay ->
                        val selectedDayCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, currentYear)
                            set(Calendar.MONTH, currentMonth)
                            set(Calendar.DAY_OF_MONTH, selectedDay.toInt())
                        }
                        onDateSelected(selectedDayCalendar)  // Pass Calendar object instead of String
                    }
                )
            }
        }
    }
}

@Composable
fun DayItem(day: String, isSelected: Boolean, onDayClick: (String) -> Unit) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val textColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .padding(4.dp)
            .background(backgroundColor)
            .clickable { onDayClick(day) }
    ) {
        Text(text = day, color = textColor, fontSize = 16.sp)
    }
}

// Utility function to get number of days in a month
fun getDaysInMonth(month: Int, year: Int): List<String> {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month)
    calendar.set(Calendar.YEAR, year)

    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return (1..daysInMonth).map { it.toString() }
}

// Utility function to get the month name
fun getMonthName(month: Int): String {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.MONTH, month)
    val dateFormat = SimpleDateFormat("MMMM", Locale.getDefault())
    return dateFormat.format(calendar.time)
}
