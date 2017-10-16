package de.reiss.bible2net.theword.note.list


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.VISIBLE
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.note.details.NoteDetailsActivity
import kotlinx.android.synthetic.main.note_list_fragment.*


class NoteListFragment : AppFragment<NoteListViewModel>(R.layout.note_list_fragment),
        NoteClickListener {

    companion object {

        fun createInstance() = NoteListFragment()

    }

    private lateinit var listItemAdapter: NoteListItemAdapter

    override fun initViews() {
        listItemAdapter = NoteListItemAdapter(noteClickListener = this)

        with(note_list_recycler_view) {
            layoutManager = LinearLayoutManager(context)
            adapter = listItemAdapter
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
            ViewModelProviders.of(this, NoteListViewModel.Factory(
                    App.component.noteListRepository))

    override fun defineViewModel(): NoteListViewModel =
            loadViewModelProvider().get(NoteListViewModel::class.java)

    override fun initViewModelObservers() {
        viewModel.notesLiveData().observe(this, Observer<AsyncLoad<List<Note>>> {
            updateUi()
        })
    }

    override fun onAppFragmentReady() {
        tryLoadNotes()
    }

    override fun onNoteClicked(note: Note) {
        activity?.let {
            it.startActivity(NoteDetailsActivity.createIntent(
                    context = it,
                    date = note.date,
                    note = note))
        }
    }

    private fun tryLoadNotes() {
        if (viewModel.isLoadingNotes().not()) {
            viewModel.loadNotes()
        }
    }

    private fun updateUi() {
        note_list_loading.visibility = GONE
        note_list_no_notes.visibility = GONE
        note_list_recycler_view.visibility = GONE

        if (viewModel.isLoadingNotes()) {
            note_list_loading.visibility = VISIBLE
        } else {
            NoteListBuilder.buildList(viewModel.notes()).let { listItems ->
                if (listItems.isEmpty()) {
                    note_list_no_notes.visibility = VISIBLE
                } else {
                    note_list_recycler_view.visibility = VISIBLE
                    listItemAdapter.updateContent(listItems)
                }
            }
        }
    }

}