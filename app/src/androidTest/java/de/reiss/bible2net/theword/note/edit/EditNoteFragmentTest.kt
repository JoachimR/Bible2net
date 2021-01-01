package de.reiss.bible2net.theword.note.edit

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.testutil.FragmentTest
import de.reiss.bible2net.theword.testutil.assertDisplayed
import de.reiss.bible2net.theword.testutil.assertNotDisplayed
import de.reiss.bible2net.theword.testutil.assertTextInSnackbar
import de.reiss.bible2net.theword.testutil.checkIsTextSet
import de.reiss.bible2net.theword.testutil.sampleNote
import de.reiss.bible2net.theword.testutil.sampleTheWordContent
import org.junit.Before
import org.junit.Test
import java.util.Date

class EditNoteFragmentTest : FragmentTest<EditNoteFragment>() {

    private val loadNoteLiveData = MutableLiveData<AsyncLoad<Note?>>()
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
        loadNoteLiveData.postValue(AsyncLoad.success(sampleNote(0)))

        assertDisplayed(R.id.edit_note_input_root)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadSuccessButNothingFoundThenShowEmptyNote() {
        loadNoteLiveData.postValue(AsyncLoad.success(null))

        assertDisplayed(R.id.edit_note_input_root)
        checkIsTextSet { R.id.edit_note_input to "" }
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
    }

    @Test
    fun whenLoadErrorThenHideInputAndShowLoadError() {
        loadNoteLiveData.postValue(AsyncLoad.error())

        assertDisplayed(R.id.edit_note_load_error)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_input_root)
    }

    @Test
    fun whenStoreErrorThenShowInputAndShowStoreErrorSnackbar() {
        storeNoteLiveData.postValue(AsyncLoad.error())

        assertDisplayed(R.id.edit_note_input_root)
        assertNotDisplayed(R.id.edit_note_loading, R.id.edit_note_load_error)
        assertTextInSnackbar(activity.getString(R.string.edit_note_store_error))
    }
}
