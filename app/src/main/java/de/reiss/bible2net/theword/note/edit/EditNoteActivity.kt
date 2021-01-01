package de.reiss.bible2net.theword.note.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.databinding.EditNoteActivityBinding
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.*

class EditNoteActivity : AppActivity() {

    companion object {
        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_THE_WORD_CONTENT = "KEY_THE_WORD_CONTENT"

        fun createIntent(context: Context, date: Date, theWordContent: TheWordContent): Intent =
                Intent(context, EditNoteActivity::class.java)
                        .putExtra(KEY_TIME, date.withZeroDayTime().time)
                        .putExtra(KEY_THE_WORD_CONTENT, theWordContent)
    }

    private lateinit var binding: EditNoteActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditNoteActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.editNoteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.edit_note_fragment_container) == null) {
            val time = intent.getLongExtra(KEY_TIME, -1L)
            if (time == -1L) {
                throw IllegalStateException("No time given for note")
            }
            val theWordContent = intent.getParcelableExtra<TheWordContent>(KEY_THE_WORD_CONTENT)
                    ?: throw IllegalStateException("No word given for note")

            replaceFragmentIn(
                    container = R.id.edit_note_fragment_container,
                    fragment = EditNoteFragment.createInstance(time, theWordContent))
        }
    }
}
