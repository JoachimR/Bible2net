package de.reiss.bible2net.theword.main.content

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.util.contentAsString

class ShareDialog : DialogFragment() {

    companion object {

        private val KEY_INITIAL_CONTENT = "KEY_INITIAL_CONTENT"

        fun createInstance(context: Context,
                           time: Long,
                           theWordContent: TheWordContent,
                           note: String) = ShareDialog().apply {
            arguments = Bundle().apply {
                putString(KEY_INITIAL_CONTENT,
                        contentAsString(context, time, theWordContent, note))
            }
        }

    }

    private lateinit var input: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
            activity.let { activity ->
                if (activity == null) {
                    throw NullPointerException()
                }
                AlertDialog.Builder(activity)
                        .setTitle(R.string.share_dialog_title)
                        .setNegativeButton(R.string.dialog_cancel, null)
                        .setPositiveButton(R.string.share_dialog_ok) { _, _ ->
                            startActivity(shareIntent())
                            dismiss()
                        }
                        .setView(createLayout(activity))
                        .create()
            }

    @SuppressLint("InflateParams")
    private fun createLayout(activity: Activity) =
            activity.layoutInflater.inflate(R.layout.share_dialog, null).apply {
                input = findViewById<EditText>(R.id.share_dialog_input).apply {
                    setText(arguments?.getString(KEY_INITIAL_CONTENT) ?: "")
                }
            }

    private fun shareIntent() = Intent.createChooser(Intent()
            .setAction(Intent.ACTION_SEND)
            .putExtra(Intent.EXTRA_TEXT, input.text.toString())
            .setType("text/plain"), resources.getText(R.string.share_dialog_chooser_title))

}