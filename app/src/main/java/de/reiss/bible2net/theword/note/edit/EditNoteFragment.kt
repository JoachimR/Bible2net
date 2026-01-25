package de.reiss.bible2net.theword.note.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.databinding.EditNoteFragmentBinding
import de.reiss.bible2net.theword.main.content.ShareDialog
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.util.extensions.hideKeyboard
import de.reiss.bible2net.theword.util.extensions.onClick
import de.reiss.bible2net.theword.util.extensions.showLongSnackbar
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.Date

class EditNoteFragment : AppFragment<EditNoteFragmentBinding, EditNoteViewModel>(
    R.layout.edit_note_fragment
) {

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
                        displayDialog(
                            ShareDialog.createInstance(
                                context = activity,
                                time = note.date.time,
                                theWordContent = note.theWordContent,
                                note = note.noteText
                            )
                        )
                        return true
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProvider(
            this,
            EditNoteViewModel.Factory(
                App.component.editNoteRepository
            )
        )

    override fun defineViewModel(): EditNoteViewModel =
        loadViewModelProvider()[EditNoteViewModel::class.java]

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        EditNoteFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        binding.editNoteLoadErrorRetry.onClick {
            tryLoad()
        }
    }

    override fun initViewModelObservers() {
        viewModel.loadNoteLiveData().observe(this) {
            updateUi()
        }
        viewModel.storeNoteLiveData().observe(this) {
            updateUi()
        }
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
                binding.editNoteLoading.setLoading(true)
                binding.editNoteInputRoot.visibility = GONE
                binding.editNoteLoadError.visibility = GONE
            }
            viewModel.loadError() -> {
                binding.editNoteLoading.setLoading(false)
                binding.editNoteInputRoot.visibility = GONE
                binding.editNoteLoadError.visibility = VISIBLE
            }
            viewModel.storeError() -> {
                binding.editNoteLoading.setLoading(false)
                binding.editNoteInputRoot.visibility = VISIBLE
                binding.editNoteLoadError.visibility = GONE
                showLongSnackbar(
                    message = R.string.edit_note_store_error,
                    action = {
                        tryStore()
                    },
                    callback = {
                        viewModel.onStoreErrorShown()
                    }
                )
            }
            else -> {
                binding.editNoteLoading.setLoading(false)
                binding.editNoteInputRoot.visibility = VISIBLE
                binding.editNoteLoadError.visibility = GONE

                if (preFillTextDone.not()) {
                    viewModel.note()?.let {
                        binding.editNoteInput.setText(it.noteText)
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
                text = binding.editNoteInput.text.toString(),
                theWordContent = theWordContent
            )
        }
    }
}
