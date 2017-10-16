package de.reiss.bible2net.theword.testutil

import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.*

fun dateForNumber(number: Int): Date {
    return Calendar.getInstance().apply {
        add(Calendar.DAY_OF_YEAR, number)
    }.time.withZeroDayTime()
}
