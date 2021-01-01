package de.reiss.bible2net.theword.note.list

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.databinding.NoteListFragmentBinding
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.note.details.NoteDetailsActivity

class NoteListFragment :
    AppFragment<NoteListFragmentBinding, NoteListViewModel>(
        R.layout.note_list_fragment
    ),
    NoteClickListener {

    companion object {
        fun createInstance() = NoteListFragment()
    }

    private val listItemAdapter = NoteListItemAdapter(noteClickListener = this)

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        NoteListFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        with(binding.noteListRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = listItemAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProviders.of(
            this,
            NoteListViewModel.Factory(
                App.component.noteListRepository
            )
        )

    override fun defineViewModel(): NoteListViewModel =
        loadViewModelProvider().get(NoteListViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.notesLiveData().observe(
            this,
            Observer<AsyncLoad<FilteredNotes>> {
                updateUi()
            }
        )
    }

    override fun onResume() {
        super.onResume()
        tryLoadNotes()
    }

    override fun onNoteClicked(note: Note) {
        activity?.let {
            it.startActivity(NoteDetailsActivity.createIntent(context = it, note = note))
        }
    }

    fun applyFilter(query: String) {
        tryRefreshFilter(query)
    }

    private fun tryLoadNotes() {
        if (viewModel.isLoadingNotes().not()) {
            viewModel.loadNotes()
        }
    }

    private fun tryRefreshFilter(query: String) {
        if (viewModel.isLoadingNotes().not()) {
            viewModel.applyNewFilter(query)
        }
    }

    private fun updateUi() {
        if (viewModel.isLoadingNotes()) {
            binding.noteListLoading.setLoading(true)
            binding.noteListNoNotes.visibility = GONE
            binding.noteListRecyclerView.visibility = GONE
        } else {
            binding.noteListLoading.setLoading(false)
            val filteredNotes = viewModel.notes()

            val listItems = NoteListBuilder.buildList(filteredNotes.filteredItems)
            if (listItems.isEmpty()) {
                binding.noteListRecyclerView.visibility = GONE
                binding.noteListNoNotes.visibility = VISIBLE
                binding.noteListNoNotesText.text =
                    if (filteredNotes.allItems.isEmpty()) {
                        getString(R.string.no_notes)
                    } else {
                        getString(R.string.no_notes_for_filter, filteredNotes.query)
                    }
            } else {
                binding.noteListRecyclerView.visibility = VISIBLE
                binding.noteListNoNotes.visibility = GONE
                listItemAdapter.updateContent(listItems)
            }
        }
    }
}
