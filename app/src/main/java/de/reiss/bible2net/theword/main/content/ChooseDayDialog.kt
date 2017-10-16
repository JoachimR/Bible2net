package de.reiss.bible2net.theword.main.content

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import de.reiss.bible2net.theword.DaysPositionUtil
import de.reiss.bible2net.theword.events.ViewPagerMoveRequest
import de.reiss.bible2net.theword.events.eventBus
import java.util.*

class ChooseDayDialog : DialogFragment() {

    companion object {

        private val KEY_INITIAL_POSITION = "KEY_INITIAL_POSITION"

        fun createInstance(initialPosition: Int) = ChooseDayDialog().apply {
            arguments = Bundle().apply {
                putInt(KEY_INITIAL_POSITION, initialPosition)
            }
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val position = arguments.getInt(KEY_INITIAL_POSITION, -1)
        if (position == -1) {
            throw IllegalStateException("no position given")
        }
        val calendar = DaysPositionUtil.dayFor(position)
        return DatePickerDialog(activity,
                onDateSetListener(),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
    }

    private fun onDateSetListener() =
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->

                val dateSetByUser = Calendar.getInstance().apply {
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }

                eventBus.post(ViewPagerMoveRequest(DaysPositionUtil.positionFor(dateSetByUser)))
            }

}