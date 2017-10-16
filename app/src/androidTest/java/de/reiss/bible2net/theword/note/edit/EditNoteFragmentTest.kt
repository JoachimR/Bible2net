package de.reiss.bible2net.theword.note.edit

import android.arch.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.testutil.*
import org.junit.Before
import org.junit.Test
import java.util.*

class EditNoteFragmentTest : FragmentTest<EditNoteFragment>() {

    private val loadNoteLiveData = MutableLiveData<AsyncLoad<Note>>()
    private val storeNoteLiveData = MutableLiveData<AsyncLoad<Void>>()

    private val mockedViewModel = mock<EditNoteViewModel> {
        on { loadNoteLiveData() } doReturn loadNoteLiveData
        on { storeNoteLiveData() } doReturn storeNoteLiveData
    }

    override fun createFragment(): EditNoteFragment =
            EditNoteFragment.createInstance(Date().time, sampleTheWordContent(0))
                    .apply {
                        viewModelProvider = mock {
                            on { get(any<Class<EditNoteViewModel>>()) } doReturn mockedViewModel
                        }
                    }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenLoadingThenShowLoading() {
        loadNoteLiveData.postValue(AsyncLoad.loading())

        assertDisplayed(R.id.edit_note_loading)
        assertNotDisplayed(R.id.edit_note_input_root, R.id.edit_note_load_error)
    }

    @Test
    fun whenStoringThenShowLoading() {
        storeNoteLiveData.postValue(AsyncLoad.loading())

        assertDisplayed(R.id.edit_note_loading)
        assertNotDisplayed(R.id.edit_note_input_root, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadSuccessThenShowInput() {
        loadNoteLiveData.postValue(AsyncLoad.success())

        assertDisplayed(R.id.edit_note_input_root)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadErrorThenHideInputAndShowLoadError() {
        loadNoteLiveData.postValue(AsyncLoad.error(message = ""))

        assertDisplayed(R.id.edit_note_load_error)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_input_root)
    }

    @Test
    fun whenStoreErrorThenShowInputAndShowStoreErrorSnackbar() {
        storeNoteLiveData.postValue(AsyncLoad.error(message = ""))

        assertDisplayed(R.id.edit_note_input_root)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
        assertTextInSnackbar(activity.getString(R.string.edit_note_store_error))
    }

}