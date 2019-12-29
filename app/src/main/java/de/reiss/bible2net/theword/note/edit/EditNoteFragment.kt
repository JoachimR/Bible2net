package de.reiss.bible2net.theword.note.edit

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.main.content.ShareDialog
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.util.extensions.hideKeyboard
import de.reiss.bible2net.theword.util.extensions.onClick
import de.reiss.bible2net.theword.util.extensions.showLongSnackbar
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import kotlinx.android.synthetic.main.edit_note_fragment.*
import java.util.*

class EditNoteFragment : AppFragment<EditNoteViewModel>(R.layout.edit_note_fragment) {

    companion object {

        private const val KEY_TIME = "KEY_TIME"
        private const val KEY_THE_WORD_CONTENT = "KEY_THE_WORD_CONTENT"
        private const val KEY_PRE_FILL_TEXT_DONE = "KEY_PRE_FILL_TEXT_DONE"

        fun createInstance(time: Long, theWordContent: TheWordContent) = EditNoteFragment().apply {
            arguments = Bundle().apply {
                putLong(KEY_TIME, time)
                putParcelable(KEY_THE_WORD_CONTENT, theWordContent)
            }
        }

    }

    private var time = -1L
    private lateinit var theWordContent: TheWordContent

    private var preFillTextDone = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        time = arguments?.getLong(KEY_TIME, -1) ?: -1
        if (time < 0) {
            throw IllegalStateException("no time given")
        }
        theWordContent = arguments?.getParcelable(KEY_THE_WORD_CONTENT)
                ?: throw IllegalStateException("no word content given")

        preFillTextDone = savedInstanceState?.getBoolean(KEY_PRE_FILL_TEXT_DONE) ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(KEY_PRE_FILL_TEXT_DONE, preFillTextDone)
        super.onSaveInstanceState(outState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_edit_note, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        viewModel.isLoadingOrStoring().let { loading ->
            menu.findItem(R.id.menu_edit_note_save).isVisible = loading.not()
            menu.findItem(R.id.menu_edit_note_save_disabled).isVisible = loading
        }
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_note_save -> {
                activity?.hideKeyboard()
                tryStore()
                return true
            }

            R.id.menu_edit_note_share -> {
                activity?.let { activity ->
                    viewModel.note()?.let { note ->
                        activity.hideKeyboard()
                        displayDialog(ShareDialog.createInstance(
                                context = activity,
                                time = note.date.time,
                                theWordContent = note.theWordContent,
                                note = note.noteText))
                        return true
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, EditNoteViewModel.Factory(
                    App.component.editNoteRepository))

    override fun defineViewModel(): EditNoteViewModel =
            loadViewModelProvider().get(EditNoteViewModel::class.java)

    override fun initViews() {
        edit_note_load_error_retry.onClick {
            tryLoad()
        }
    }

    override fun initViewModelObservers() {
        viewModel.loadNoteLiveData().observe(this, Observer<AsyncLoad<Note?>> {
            updateUi()
        })
        viewModel.storeNoteLiveData().observe(this, Observer<AsyncLoad<Void>> {
            updateUi()
        })
    }

    override fun onAppFragmentReady() {
        if (viewModel.note() == null) {
            tryLoad()
        }
    }

    private fun updateUi() {
        when {
            viewModel.storeSuccess() -> {
                activity?.supportFinishAfterTransition()
            }

            viewModel.isLoadingOrStoring() -> {
                edit_note_loading.loading = true
                edit_note_input_root.visibility = GONE
                edit_note_load_error.visibility = GONE
            }

            viewModel.loadError() -> {
                edit_note_loading.loading = false
                edit_note_input_root.visibility = GONE
                edit_note_load_error.visibility = VISIBLE
            }

            viewModel.storeError() -> {
                edit_note_loading.loading = false
                edit_note_input_root.visibility = VISIBLE
                edit_note_load_error.visibility = GONE
                showLongSnackbar(
                        message = R.string.edit_note_store_error,
                        action = {
                            tryStore()
                        },
                        callback = {
                            viewModel.onStoreErrorShown()
                        })
            }

            else -> {
                edit_note_loading.loading = false
                edit_note_input_root.visibility = VISIBLE
                edit_note_load_error.visibility = GONE

                if (preFillTextDone.not()) {
                    viewModel.note()?.let {
                        edit_note_input.setText(it.noteText)
                        preFillTextDone = true
                    }
                }
            }
        }

        activity?.invalidateOptionsMenu()
    }

    private fun tryLoad() {
        if (viewModel.isLoadingOrStoring().not()) {
            viewModel.loadNote(Date(time).withZeroDayTime())
        }
    }

    private fun tryStore() {
        if (viewModel.isLoadingOrStoring().not()) {
            viewModel.storeNote(
                    date = Date(time).withZeroDayTime(),
                    text = edit_note_input.text.toString(),
                    theWordContent = theWordContent
            )
        }
    }

}
