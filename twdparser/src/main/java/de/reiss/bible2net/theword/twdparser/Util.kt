package de.reiss.bible2net.theword.twdparser

import android.annotation.SuppressLint
import de.reiss.bible2net.theword.logger.logWarn
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@SuppressLint("SimpleDateFormat")
private fun simpleDateFormat() = SimpleDateFormat("yyyy-MM-dd") // use function for thread safety

fun dateFromString(dateString: String?): Date? {
    try {
        if (dateString != null) {
            return simpleDateFormat().parse(dateString)
        }
    } catch (e: Exception) {
        logWarn(e) { "Error when trying to parse date from string $dateString" }
    }
    return null
}
