package de.reiss.bible2net.theword.note.details

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import de.reiss.bible2net.theword.testutil.sampleNote
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NoteDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val note = sampleNote(0)

    private lateinit var viewModel: NoteDetailsViewModel
    private val repository: NoteDetailsRepository = mock()

    @Before
    fun setUp() {
        viewModel = NoteDetailsViewModel(note, repository)
    }

    @Test
    fun loadNote() {
        viewModel.loadNote()
        verify(repository).loadNote(eq(note), any())
    }

    @Test
    fun deleteNote() {
        viewModel.deleteNote()
        verify(repository).deleteNote(eq(note), any())
    }

}