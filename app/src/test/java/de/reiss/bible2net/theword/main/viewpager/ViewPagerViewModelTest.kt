package de.reiss.bible2net.theword.main.viewpager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import de.reiss.bible2net.theword.util.extensions.firstDayOfYear
import de.reiss.bible2net.theword.util.extensions.lastDayOfYear
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@Suppress("IllegalIdentifier")
class ViewPagerViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ViewPagerViewModel
    private val repository: ViewPagerRepository = mock()

    private val bible = "testBible"

    private val date = Date()


    @Before
    fun setUp() {
        viewModel = ViewPagerViewModel(initialBible = bible, repository = repository)
    }

    @Test
    fun `when prepare is called then ask repo for whole year`() {
        viewModel.prepareContentFor(bible, date)

        val bibleCaptor = argumentCaptor<String>()
        val dateCaptor = argumentCaptor<Date>()

        verify(repository).getItemsFor(
                bible = bibleCaptor.capture(),
                fromDate = dateCaptor.capture(),
                toDate = dateCaptor.capture(),
                result = any())

        assertEquals(bible, bibleCaptor.firstValue)
        assertEquals(date.firstDayOfYear(), dateCaptor.firstValue)
        assertEquals(date.lastDayOfYear(), dateCaptor.secondValue)
    }

}