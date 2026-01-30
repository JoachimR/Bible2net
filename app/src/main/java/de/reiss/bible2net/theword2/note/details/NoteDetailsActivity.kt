package de.reiss.bible2net.theword2.note.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.bible2net.theword2.R
import de.reiss.bible2net.theword2.architecture.AppActivity
import de.reiss.bible2net.theword2.databinding.NoteDetailsActivityBinding
import de.reiss.bible2net.theword2.model.Note
import de.reiss.bible2net.theword2.util.extensions.findFragmentIn
import de.reiss.bible2net.theword2.util.extensions.replaceFragmentIn

class NoteDetailsActivity : AppActivity(), ConfirmDeleteDialog.Listener {

    companion object {
        private const val KEY_NOTE = "KEY_NOTE"

        fun createIntent(context: Context, note: Note): Intent =
            Intent(context, NoteDetailsActivity::class.java)
                .putExtra(KEY_NOTE, note)
    }

    private lateinit var binding: NoteDetailsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteDetailsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.noteDetailsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findNoteDetailsFragment() == null) {
            replaceFragmentIn(
                container = R.id.note_details_fragment,
                fragment = NoteDetailsFragment.createInstance(intent.getParcelableExtra(KEY_NOTE)!!)
            )
        }
    }

    override fun onDeleteConfirmed() {
        findNoteDetailsFragment()?.tryDeleteNote()
    }

    private fun findNoteDetailsFragment() =
        findFragmentIn(R.id.note_details_fragment) as? NoteDetailsFragment
}
