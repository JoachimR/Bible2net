package de.reiss.bible2net.theword.util.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

fun Date.asDateString(): String = dateFormat.format(this)

fun Date.withZeroDayTime(): Date =
        Calendar.getInstance().apply {
            time = this@withZeroDayTime
            minDateTime()
        }.time


fun Date.extractYear(): Int =
        Calendar.getInstance().apply {
            time = this@extractYear
        }.get(Calendar.YEAR)

fun Date.firstDayOfYear(): Date =
        Calendar.getInstance().apply {
            time = this@firstDayOfYear
            set(Calendar.DAY_OF_YEAR, 1)
        }.time

fun Date.lastDayOfYear(): Date =
        Calendar.getInstance().apply {
            time = this@lastDayOfYear
            set(Calendar.DAY_OF_YEAR, 365)
        }.time

fun Date.amountOfDaysInRange(otherEndOfRange: Date) =
        TimeUnit.MILLISECONDS.toDays(Math.abs(time - otherEndOfRange.time)).toInt() + 1