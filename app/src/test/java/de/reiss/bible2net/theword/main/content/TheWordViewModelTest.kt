package de.reiss.bible2net.theword.main.content

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.*

@RunWith(JUnit4::class)
class TheWordViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TheWordViewModel
    private val repository: TheWordRepository = mock()

    @Before
    fun setUp() {
        viewModel = TheWordViewModel(repository)
    }

    @Test
    fun loadTheWord() {
        val date = Date()

        viewModel.loadTheWord(date)

        argumentCaptor<Date>().apply {
            verify(repository).getTheWordFor(capture(), any())
            assertEquals(date, allValues[0])
        }
    }

    @Test
    fun loadNote() {
        val date = Date()

        viewModel.loadNote(date)

        argumentCaptor<Date>().apply {
            verify(repository).getNoteFor(capture(), any())
            assertEquals(date, allValues[0])
        }
    }

}