package de.reiss.bible2net.theword.main.content

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.SeekBar
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.preferences.AppPreferences

class FontSizePreferenceDialog : DialogFragment() {

    companion object {

        fun createInstance() = FontSizePreferenceDialog()

    }

    private val appPreferences: AppPreferences by lazy {
        App.component.appPreferences
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            activity.let { activity ->
                if (activity == null) {
                    throw NullPointerException()
                }
                AlertDialog.Builder(activity)
                        .setTitle(getString(R.string.fontsize_dialog_title))
                        .setCancelable(true)
                        .setPositiveButton(activity.getString(R.string.fontsize_dialog_ok)) { _, _ ->
                            dismiss()
                        }
                        .setView(initDialogUi())
                        .create()
            }

    private fun initDialogUi(): View {
        activity.let { activity ->
            if (activity == null) {
                throw NullPointerException()
            }

            val view = activity.layoutInflater.inflate(R.layout.fontsize_dialog, null)

            view.findViewById<SeekBar>(R.id.fontsize_dialog_seekbar).apply {

                progress = appPreferences.fontSize()

                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                    override fun onProgressChanged(seekBar: SeekBar,
                                                   progress: Int,
                                                   fromUser: Boolean) {
                        if (fromUser) {
                            appPreferences.changeFontSize(newFontSize = progress)
                        }
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar) {
                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar) {
                    }

                })
            }

            return view
        }
    }

}
