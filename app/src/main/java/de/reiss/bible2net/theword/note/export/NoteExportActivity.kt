package de.reiss.bible2net.theword.note.export

import android.content.Context
import android.content.Intent
import android.os.Bundle
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppActivity
import de.reiss.bible2net.theword.databinding.NoteExportActivityBinding
import de.reiss.bible2net.theword.util.extensions.findFragmentIn
import de.reiss.bible2net.theword.util.extensions.replaceFragmentIn

class NoteExportActivity : AppActivity() {

    companion object {
        fun createIntent(context: Context): Intent =
            Intent(context, NoteExportActivity::class.java)
    }

    private lateinit var binding: NoteExportActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteExportActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.noteExportToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (findFragmentIn(R.id.note_export_fragment) == null) {
            replaceFragmentIn(
                container = R.id.note_export_fragment,
                fragment = NoteExportFragment.createInstance()
            )
        }
    }
}
