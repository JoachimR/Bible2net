package de.reiss.bible2net.theword.note.details

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.testutil.*
import de.reiss.bible2net.theword.util.contentAsString
import org.junit.Before
import org.junit.Test

class NoteDetailsFragmentTest : FragmentTest<NoteDetailsFragment>() {

    private val noteLiveData = MutableLiveData<AsyncLoad<Note>>()
    private val deleteLiveData = MutableLiveData<AsyncLoad<Void>>()

    val note = sampleNote(0)

    private val mockedViewModel = mock<NoteDetailsViewModel> {
        on { noteLiveData() } doReturn noteLiveData
        on { deleteLiveData() } doReturn deleteLiveData
    }

    override fun createFragment(): NoteDetailsFragment =
            NoteDetailsFragment.createInstance(note)
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<NoteDetailsViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        noteLiveData.postValue(AsyncLoad.loading())
        assertDisplayed(R.id.note_details_loading)
    }

    @Test
    fun whenDeletingThenShowLoading() {
        deleteLiveData.postValue(AsyncLoad.loading())
        assertDisplayed(R.id.note_details_loading)
    }

    @Test
    fun whenDeletedThenFinishing() {
        deleteLiveData.postValue(AsyncLoad.success())
        assertActivityIsFinished(activityRule)
    }

    @Test
    fun whenLoadSuccessThenShowContent() {
        noteLiveData.postValue(AsyncLoad.success(note))

        checkTextsAreDisplayed(
                contentAsString(activity, note.date.time, note.theWordContent, ""),
                note.noteText)

        assertNotDisplayed(R.id.note_details_loading)
    }

}