package de.reiss.bible2net.theword.main.viewpager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.never
import com.nhaarman.mockito_kotlin.times
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword.database.BibleItem
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.TheWordItem
import de.reiss.bible2net.theword.database.TheWordItemDao
import de.reiss.bible2net.theword.downloader.file.FileDownloader
import de.reiss.bible2net.theword.downloader.list.ListDownloader
import de.reiss.bible2net.theword.testutil.TestExecutor
import de.reiss.bible2net.theword.testutil.blockingObserve
import de.reiss.bible2net.theword.testutil.sampleTheWordItem
import de.reiss.bible2net.theword.util.extensions.firstDayOfYear
import de.reiss.bible2net.theword.util.extensions.lastDayOfYear
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date

@Suppress("IllegalIdentifier")
class ViewPagerRepositoryTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var repository: ViewPagerRepository

    private val theWordItemDao = mock<TheWordItemDao>()
    private val bibleItemDao = mock<BibleItemDao>()

    private val listDownloader = mock<ListDownloader>()
    private val fileDownloader = mock<FileDownloader>()

    private val bibleItem = BibleItem("testBible", "testBibleName", "testLanguageCode")
        .apply { id = 1 }

    private val date = Date().withZeroDayTime()

    @Before
    fun setUp() {
        repository = ViewPagerRepository(
            TestExecutor(),
            listDownloader,
            fileDownloader,
            theWordItemDao,
            bibleItemDao
        )
    }

    @Test
    fun `when bible not found then repo tries to download`() {
        whenever(bibleItemDao.all()).thenReturn(emptyList())
        whenever(bibleItemDao.find(any())).thenReturn(null)

        val liveData = MutableLiveData<AsyncLoad<String>>()
        repository.getItemsFor(
            bible = bibleItem.bible,
            fromDate = date.firstDayOfYear(),
            toDate = date.lastDayOfYear(),
            result = liveData
        )
        val result = liveData.blockingObserve() ?: throw NullPointerException()
        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
    }

    @Test
    fun `when 0 items available then repo tries to download`() {
        mockBibleDatabaseResult(bibleItem)
        setItemsAvailable(0)

        // repo tries to download list but fails downloading
        verify(listDownloader, times(1)).downloadList()
    }

    @Test
    fun `when 99 items available then repo tries to download`() {
        mockBibleDatabaseResult(bibleItem)
        setItemsAvailable(99)

        // repo tries to download because some items are missing for the current year
        verify(listDownloader, times(1)).downloadList()
    }

    @Test
    fun `when 365 items available then no download happening`() {
        mockBibleDatabaseResult(bibleItem)
        setItemsAvailable(365)

        // no need for download
        verify(listDownloader, never()).downloadList()
    }

    @Test
    fun `when 366 items available then no download happening`() {
        mockBibleDatabaseResult(bibleItem)
        setItemsAvailable(366)

        // no need for download
        verify(listDownloader, never()).downloadList()
    }

    private fun setItemsAvailable(amount: Int) {
        val list = (0 until amount).map { sampleTheWordItem(it + 1, bibleItem.id) }
        mockTheWordDatabaseResult(result = list)
        loadItemsFromRepo()
    }

    private fun loadItemsFromRepo() {
        val liveData = MutableLiveData<AsyncLoad<String>>()
        repository.getItemsFor(
            bible = bibleItem.bible,
            fromDate = date.firstDayOfYear(),
            toDate = date.lastDayOfYear(),
            result = liveData
        )
        val result = liveData.blockingObserve() ?: throw NullPointerException()
        assertEquals(AsyncLoadStatus.SUCCESS, result.loadStatus)
        assertEquals(bibleItem.bible, result.data)
    }

    private fun mockTheWordDatabaseResult(result: List<TheWordItem>) {
        whenever(theWordItemDao.range(any(), any(), any()))
            .thenReturn(result)

        whenever(theWordItemDao.all())
            .thenReturn(result)
    }

    private fun mockBibleDatabaseResult(bibleItem: BibleItem) {
        whenever(bibleItemDao.all()).thenReturn(listOf(bibleItem))
        whenever(bibleItemDao.find(any())).thenReturn(bibleItem)
    }
}
