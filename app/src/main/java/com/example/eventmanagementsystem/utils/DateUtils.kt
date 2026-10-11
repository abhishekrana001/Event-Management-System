package com.example.eventmanagementsystem.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtils {

    private val datePatterns = arrayOf(
        "dd/MM/yyyy",
        "d/M/yyyy",
        "yyyy-MM-dd",
        "dd-MM-yyyy"
    )

    fun parseDate(dateStr: String): Date? {
        if (dateStr.isBlank()) return null
        for (pattern in datePatterns) {
            try {
                val format = SimpleDateFormat(pattern, Locale.getDefault()).apply {
                    isLenient = false
                }
                val parsed = format.parse(dateStr.trim())
                if (parsed != null) return parsed
            } catch (_: Exception) {
            }
        }
        return null
    }

    fun parseDateTime(dateStr: String, timeStr: String): Date? {
        val baseDate = parseDate(dateStr) ?: return null
        if (timeStr.isBlank()) return baseDate

        val timePatterns = arrayOf(
            "hh:mm a",
            "h:mm a",
            "HH:mm",
            "H:m"
        )

        for (pattern in timePatterns) {
            try {
                val tf = SimpleDateFormat(pattern, Locale.getDefault()).apply {
                    isLenient = true
                }
                val parsedTime = tf.parse(timeStr.trim())
                if (parsedTime != null) {
                    val calDate = Calendar.getInstance().apply { time = baseDate }
                    val calTime = Calendar.getInstance().apply { time = parsedTime }

                    calDate[Calendar.HOUR_OF_DAY] = calTime[Calendar.HOUR_OF_DAY]
                    calDate[Calendar.MINUTE] = calTime[Calendar.MINUTE]
                    calDate[Calendar.SECOND] = 0
                    calDate[Calendar.MILLISECOND] = 0

                    return calDate.time
                }
            } catch (_: Exception) {
            }
        }

        return baseDate
    }

    fun isEventExpired(dateStr: String, timeStr: String): Boolean {
        val eventDateTime = parseDateTime(dateStr, timeStr) ?: return false
        return eventDateTime.before(Date())
    }

    fun isPastDate(dateStr: String): Boolean {
        val parsed = parseDate(dateStr) ?: return false
        val todayCalendar = Calendar.getInstance().apply {
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
        }
        val eventCalendar = Calendar.getInstance().apply {
            time = parsed
            this[Calendar.HOUR_OF_DAY] = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
            this[Calendar.MILLISECOND] = 0
        }
        return eventCalendar.before(todayCalendar)
    }

    fun formatDateForDisplay(dateStr: String): String {
        val parsed = parseDate(dateStr) ?: return dateStr
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return outputFormat.format(parsed)
    }
}
