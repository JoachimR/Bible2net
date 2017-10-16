package de.reiss.bible2net.theword.note.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.main.content.ShareDialog
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.util.contentAsString
import de.reiss.bible2net.theword.util.extensions.displayDialog
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import kotlinx.android.synthetic.main.note_details_activity.*
import java.util.*

class NoteDetailsActivity : AppActivity() {

    companion object {

        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_NOTE = "KEY_NOTE"

        fun createIntent(context: Context, date: Date, note: Note): Intent =
                Intent(context, NoteDetailsActivity::class.java)
                        .putExtra(KEY_TIME, date.withZeroDayTime().time)
                        .putExtra(KEY_NOTE, note)

    }

    private var time: Long = -1L
    private lateinit var note: Note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_details_activity)
        setSupportActionBar(note_details_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        time = intent.getLongExtra(KEY_TIME, -1L)
        if (time == -1L) throw IllegalStateException("no time given")
        note = intent.getParcelableExtra(KEY_NOTE)
                ?: throw IllegalStateException("no note given")

        note_details_text.text = contentAsString(this, time, note.theWordContent, note.noteText)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) =
            when (item.itemId) {
                R.id.menu_note_details_share -> {
                    displayDialog(ShareDialog.createInstance(
                            context = this,
                            time = time,
                            theWordContent = note.theWordContent,
                            note = note.noteText))
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }

}