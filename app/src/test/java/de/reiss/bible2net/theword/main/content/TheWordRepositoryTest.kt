package de.reiss.bible2net.theword.main.content

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus.ERROR
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus.SUCCESS
import de.reiss.bible2net.theword.database.BibleItem
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.TheWordItemDao
import de.reiss.bible2net.theword.main.BibleListUpdater
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.testutil.TestExecutor
import de.reiss.bible2net.theword.testutil.blockingObserve
import de.reiss.bible2net.theword.testutil.sampleNoteItem
import de.reiss.bible2net.theword.testutil.sampleTheWordItem
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@Suppress("IllegalIdentifier")
class TheWordRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: TheWordRepository

    private val theWordItemDao: TheWordItemDao = mock()
    private val bibleListUpdater: BibleListUpdater = mock()
    private val bibleItemDao: BibleItemDao = mock()
    private val noteItemDao: NoteItemDao = mock()
    private val appPreferences: AppPreferences = mock()

    private val bibleItem = BibleItem("testBible", "testBibleName", "testLanguageCode")
            .apply { id = 1 }

    private val date = Date().withZeroDayTime()

    @Before
    fun setUp() {
        whenever(appPreferences.chosenBible).thenReturn(bibleItem.bible)
        whenever(bibleItemDao.find(any())).thenReturn(bibleItem)

        repository = TheWordRepository(
                executor = TestExecutor(),
                bibleListUpdater = bibleListUpdater,
                theWordItemDao = theWordItemDao,
                bibleItemDao = bibleItemDao,
                noteItemDao = noteItemDao,
                appPreferences = appPreferences)
    }

    @Test
    fun `when word not found return error with message`() {
        whenever(theWordItemDao.byDate(any(), any()))
                .thenReturn(null)


        val result = loadTheWordFromRepo()

        assertEquals(ERROR, result.loadStatus)
        assertEquals("Content not found", result.message)
    }

    @Test
    fun `when word found return word`() {
        val item = sampleTheWordItem(number = 0, bibleId = bibleItem.id)

        whenever(theWordItemDao.byDate(any(), any()))
                .thenReturn(item)

        val result = loadTheWordFromRepo()

        assertEquals(SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(bibleItem.bible, data.bible)
        assertEquals(date, data.date)
        assertEquals(item.book1, data.content.book1)
        assertEquals(item.chapter1, data.content.chapter1)
        assertEquals(item.verse1, data.content.verse1)
        assertEquals(item.id1, data.content.id1)
        assertEquals(item.intro1, data.content.intro1)
        assertEquals(item.text1, data.content.text1)
        assertEquals(item.ref1, data.content.ref1)
        assertEquals(item.book2, data.content.book2)
        assertEquals(item.chapter2, data.content.chapter2)
        assertEquals(item.verse2, data.content.verse2)
        assertEquals(item.id2, data.content.id2)
        assertEquals(item.intro2, data.content.intro2)
        assertEquals(item.text2, data.content.text2)
        assertEquals(item.ref2, data.content.ref2)
    }

    @Test
    fun `when note not found return null`() {
        whenever(noteItemDao.byDate(any()))
                .thenReturn(null)


        val result = loadNoteFromRepo()

        assertEquals(SUCCESS, result.loadStatus)
        assertNull(result.data)
    }

    @Test
    fun `when note found return note`() {
        val item = sampleNoteItem(number = 0)

        whenever(noteItemDao.byDate(any()))
                .thenReturn(item)

        val result = loadNoteFromRepo()

        assertEquals(SUCCESS, result.loadStatus)
        val data = result.data ?: throw NullPointerException()

        assertEquals(date, data.date)
        assertEquals(item.note, data.noteText)

        assertEquals(item.book1, data.theWordContent.book1)
        assertEquals(item.chapter1, data.theWordContent.chapter1)
        assertEquals(item.verse1, data.theWordContent.verse1)
        assertEquals(item.id1, data.theWordContent.id1)
        assertEquals(item.intro1, data.theWordContent.intro1)
        assertEquals(item.text1, data.theWordContent.text1)
        assertEquals(item.ref1, data.theWordContent.ref1)
        assertEquals(item.book2, data.theWordContent.book2)
        assertEquals(item.chapter2, data.theWordContent.chapter2)
        assertEquals(item.verse2, data.theWordContent.verse2)
        assertEquals(item.id2, data.theWordContent.id2)
        assertEquals(item.intro2, data.theWordContent.intro2)
        assertEquals(item.text2, data.theWordContent.text2)
        assertEquals(item.ref2, data.theWordContent.ref2)
    }

    private fun loadTheWordFromRepo(): AsyncLoad<TheWord> {
        val liveData = MutableLiveData<AsyncLoad<TheWord>>()
        repository.getTheWordFor(date, liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

    private fun loadNoteFromRepo(): AsyncLoad<Note> {
        val liveData = MutableLiveData<AsyncLoad<Note>>()
        repository.getNoteFor(date, liveData)
        return liveData.blockingObserve() ?: throw NullPointerException()
    }

}