package de.reiss.bible2net.theword.note.list

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword.database.NoteItem
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.testutil.TestExecutor
import de.reiss.bible2net.theword.testutil.blockingObserve
import de.reiss.bible2net.theword.testutil.sampleNoteItem
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@Suppress("IllegalIdentifier")
class NoteListRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: NoteListRepository

    private val noteItemDao = mock<NoteItemDao>()

    @Before
    fun setUp() {
        repository = NoteListRepository(TestExecutor(), noteItemDao)
    }

    @Test
    fun `when 0 items available then repo returns empty list`() {
        whenever(noteItemDao.all()).thenReturn(emptyList())

        val result = loadItemsFromRepo()

        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(0, data.size)
    }

    @Test
    fun `when 1 item available then repo returns list with 1 item`() {
        val databaseItems = listOf(sampleNoteItem(0))
        checkFor(databaseItems)
    }

    @Test
    fun `when 99 items available then repo returns list with 99 items`() {
        val databaseItems = (1..99).map { sampleNoteItem(it) }
        checkFor(databaseItems)
    }

    private fun checkFor(databaseItems: List<NoteItem>) {
        whenever(noteItemDao.all()).thenReturn(databaseItems)

        val result = loadItemsFromRepo()

        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(databaseItems.size, data.size)

        for ((index, databaseItem) in databaseItems.withIndex()) {

            val note = data[index]

            assertEquals(databaseItem.date, note.date)
            assertEquals(databaseItem.note, note.noteText)
            assertEquals(databaseItem.book1, note.theWordContent.book1)
            assertEquals(databaseItem.chapter1, note.theWordContent.chapter1)
            assertEquals(databaseItem.verse1, note.theWordContent.verse1)
            assertEquals(databaseItem.id1, note.theWordContent.id1)
            assertEquals(databaseItem.intro1, note.theWordContent.intro1)
            assertEquals(databaseItem.text1, note.theWordContent.text1)
            assertEquals(databaseItem.ref1, note.theWordContent.ref1)
            assertEquals(databaseItem.book2, note.theWordContent.book2)
            assertEquals(databaseItem.chapter2, note.theWordContent.chapter2)
            assertEquals(databaseItem.verse2, note.theWordContent.verse2)
            assertEquals(databaseItem.id2, note.theWordContent.id2)
            assertEquals(databaseItem.intro2, note.theWordContent.intro2)
            assertEquals(databaseItem.text2, note.theWordContent.text2)
            assertEquals(databaseItem.ref2, note.theWordContent.ref2)
        }
    }

    private fun loadItemsFromRepo(): AsyncLoad<List<Note>> {
        val liveData = MutableLiveData<AsyncLoad<List<Note>>>()
        repository.getAllNotes(result = liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

}