package de.reiss.bible2net.theword.note.export

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import de.reiss.bible2net.theword.database.NoteItem
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.note.export.NoteExportStatus.ExportError
import de.reiss.bible2net.theword.note.export.NoteExportStatus.ExportSuccess
import de.reiss.bible2net.theword.note.export.NoteExportStatus.NoNotes
import de.reiss.bible2net.theword.note.export.NoteExportStatus.NoPermission
import de.reiss.bible2net.theword.testutil.TestExecutor
import de.reiss.bible2net.theword.testutil.sampleNoteItem
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@Suppress("IllegalIdentifier")
class NoteExportRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: NoteExportRepository

    private val noteItemDao = mock<NoteItemDao>()
    private val notesExporter = mock<NotesExporter> {
        on { directory } doReturn "testDirectory"
        on { fileName } doReturn "testFileName"
    }

    @Before
    fun setUp() {
        repository = NoteExportRepository(TestExecutor(), noteItemDao, notesExporter)
    }

    @Test
    fun `when storage not writable then repo returns storage not writable error `() {
        whenever(notesExporter.isExternalStorageWritable()).thenReturn(false)

        val result = tryExport()

        assertTrue(result is NoPermission)
    }

    @Test
    fun `when 0 notes to export then repo returns no notes error`() {
        whenever(notesExporter.isExternalStorageWritable()).thenReturn(true)
        whenever(noteItemDao.all()).thenReturn(emptyList())

        val result = tryExport()

        assertTrue(result is NoNotes)
    }

    @Test
    fun `when 1 note to export and exporter returns true then repo returns success`() {
        whenever(notesExporter.isExternalStorageWritable()).thenReturn(true)
        whenever(notesExporter.exportNotes(any<List<NoteItem>>())).thenReturn(true)
        whenever(noteItemDao.all()).thenReturn(listOf(sampleNoteItem(0)))

        val result = tryExport()

        assertTrue(result is ExportSuccess)
    }

    @Test
    fun `when 1 note to export and exporter returns false then repo returns error`() {
        whenever(notesExporter.isExternalStorageWritable()).thenReturn(true)
        whenever(notesExporter.exportNotes(any<List<NoteItem>>())).thenReturn(false)
        whenever(noteItemDao.all()).thenReturn(listOf(sampleNoteItem(0)))

        val result = tryExport()

        assertTrue(result is ExportError)
    }

    private fun tryExport(): NoteExportStatus {
        val liveData = MutableLiveData<NoteExportStatus>()
        repository.exportNotes(liveData)
        return blockingObserve(liveData) ?: throw NullPointerException()
    }

    private fun blockingObserve(liveData: LiveData<NoteExportStatus>): NoteExportStatus? {
        var value: NoteExportStatus? = null
        val latch = CountDownLatch(1)
        val innerObserver = Observer<NoteExportStatus> {
            value = it
            latch.countDown()
        }
        liveData.observeForever(innerObserver)
        latch.await(2, TimeUnit.SECONDS)
        return value
    }
}
