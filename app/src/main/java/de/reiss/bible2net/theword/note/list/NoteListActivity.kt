package de.reiss.bible2net.theword.note.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.note.export.NoteExportActivity
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.onClick
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn
import kotlinx.android.synthetic.main.note_list_activity.*

class NoteListActivity : AppActivity() {

    companion object {

        fun createIntent(context: Context): Intent =
                Intent(context, NoteListActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_list_activity)
        setSupportActionBar(note_list_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.note_list_fragment) == null) {
            replaceFragmentIn(
                    container = R.id.note_list_fragment,
                    fragment = NoteListFragment.createInstance())
        }

        note_list_export.onClick {
            startActivity(NoteExportActivity.createIntent(this))
        }
    }
}