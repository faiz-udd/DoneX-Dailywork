package com.example.dailywork.utils

import android.text.format.DateFormat
import java.text.SimpleDateFormat
import java.util.*

// Extension Function to Format Date
fun Date.toFormattedString(pattern: String = Constants.DATE_FORMAT): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}

// Extension Function to Format Time
fun Date.toFormattedTimeString(pattern: String = Constants.TIME_FORMAT): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}

// Extension Function to Format DateTime
fun Date.toFormattedDateTimeString(pattern: String = Constants.DATE_TIME_FORMAT): String {
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(this)
}

// Extension Function to Parse String to Date
fun String.toDate(pattern: String = Constants.DATE_FORMAT): Date? {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.parse(this)
    } catch (e: Exception) {
        null
    }
}

// Extension to Check if a Date is Today
fun Date.isToday(): Boolean {
    val today = Calendar.getInstance()
    val thisDate = Calendar.getInstance().apply { time = this@isToday }
    return thisDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
            thisDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)
}

// Extension Function to Capitalize First Letter of String
fun String.capitalizeFirstLetter(): String {
    return this.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}

// Extension Function to Convert String to Time Format
fun String.toFormattedTime(pattern: String = Constants.TIME_FORMAT): Date? {
    return try {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        sdf.parse(this)
    } catch (e: Exception) {
        null
    }
}

// Extension Function to Format Int as a 2-digit String
fun Int.toTwoDigitString(): String {
    return if (this < 10) "0$this" else this.toString()
}

// Extension Function to Simplify Date Comparison
fun Date.isBefore(other: Date): Boolean {
    return this.before(other)
}

fun Date.isAfter(other: Date): Boolean {
    return this.after(other)
}

// Extension for Validating Non-Empty Input Fields
fun String?.isValidInput(): Boolean {
    return this != null && this.isNotBlank()
}
