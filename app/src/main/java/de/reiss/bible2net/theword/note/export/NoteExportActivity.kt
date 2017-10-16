package de.reiss.bible2net.theword.note.export

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.note_export_activity.*

class NoteExportActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, NoteExportActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_export_activity)
        setSupportActionBar(note_export_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.note_export_fragment) == null) {
            replaceFragmentIn(
                    container = R.id.note_export_fragment,
                    fragment = NoteExportFragment.createInstance())
        }
    }

}