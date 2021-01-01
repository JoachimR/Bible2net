package de.reiss.bible2net.theword

import de.reiss.bible2net.theword.util.extensions.maxDayTime
import java.util.Calendar
import java.util.Date

object DaysPositionUtil {

    private const val DURATION_ONE_DAY = 86400000; // (24h * 60m * 60s * 1000ms)

    private val FIRST_DAY_OF_TIME: Calendar = Calendar.getInstance().apply {
        set(1900, Calendar.JANUARY, 1)
    }

    const val DAYS_OF_TIME = 73413 // (Dec 31st 2100) - (Jan 1st 1900)

    @Throws(IllegalArgumentException::class)
    fun dayFor(position: Int): Calendar = Calendar.getInstance().apply {
        if (position < 0) {
            throw IllegalArgumentException("position cannot be negative")
        }
        time = FIRST_DAY_OF_TIME.time
        add(Calendar.DAY_OF_YEAR, position)
    }

    fun positionFor(calendar: Calendar) = calcPosition(calendar.time)

    private fun positionFor(date: Date) = calcPosition(date)

    private fun calcPosition(date: Date) =
        Math.round((millisSinceFirstDay(date) / DURATION_ONE_DAY).toFloat())

    private fun millisSinceFirstDay(date: Date) =
        Calendar.getInstance().apply {
            time = date
            maxDayTime()
        }.timeInMillis - FIRST_DAY_OF_TIME.timeInMillis
}
