package de.reiss.bible2net.theword

import android.content.Context
import android.text.format.DateUtils.*

fun formattedDate(context: Context, time: Long): String =
        formatDateTime(context,
                time,
                FORMAT_SHOW_DATE or FORMAT_SHOW_YEAR or FORMAT_SHOW_WEEKDAY)
