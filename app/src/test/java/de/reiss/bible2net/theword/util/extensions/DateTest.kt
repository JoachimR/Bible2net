package de.reiss.bible2net.theword.util.extensions

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class DateTest {

    @Test
    fun dayDiff() {

        val now = Calendar.getInstance()

        val a = now.apply {
            minDateTime()
            set(Calendar.MONTH, Calendar.JANUARY)
            set(Calendar.DAY_OF_MONTH, 1)
        }.time

        val b = now.apply {
            minDateTime()
            set(Calendar.MONTH, Calendar.DECEMBER)
            set(Calendar.DAY_OF_MONTH, 31)
        }.time

        assertEquals(1, a.amountOfDaysInRange(a))
        assertEquals(365, a.amountOfDaysInRange(b))
        assertEquals(365, b.amountOfDaysInRange(a))
        assertEquals(1, b.amountOfDaysInRange(b))
    }
}
