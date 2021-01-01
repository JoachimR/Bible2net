package de.reiss.bible2net.theword.note.export

import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.note.export.NoteExportStatus.*
import de.reiss.bible2net.theword.testutil.*
import org.junit.Before
import org.junit.Test

class NoteExportFragmentTest : FragmentTest<NoteExportFragment>() {

    private val exportLiveData = MutableLiveData<NoteExportStatus>()

    private val mockedViewModel = mock<NoteExportViewModel> {
        on { exportLiveData() } doReturn exportLiveData
    }

    override fun createFragment(): NoteExportFragment = NoteExportFragment.createInstance()
            .apply {
                viewModelProvider = mock {
                    on { get(any<Class<NoteExportViewModel>>()) } doReturn mockedViewModel
                }
            }

    @Before
    fun setUp() {
        launchFragment()
    }

    @Test
    fun whenExportingThenShowLoadingAndDisableStartButton() {
        exportLiveData.postValue(Exporting)

        assertDisplayed(R.id.note_export_loading)
        assertDisabled(R.id.note_export_start)
    }

    @Test
    fun whenNoPermissionThenShowNoPermissionMessage() {
        exportLiveData.postValue(NoPermission)

        assertNotDisplayed(R.id.note_export_loading)
        assertEnabled(R.id.note_export_start)
        assertTextInSnackbar(activity.getString(R.string.can_not_write_to_sdcard))
    }

    @Test
    fun whenNoNotesToExportThenShowNoNotesToExportMessage() {
        exportLiveData.postValue(NoNotes)

        assertNotDisplayed(R.id.note_export_loading)
        assertEnabled(R.id.note_export_start)
        assertTextInSnackbar(activity.getString(R.string.notes_export_no_notes))
    }

    @Test
    fun whenExportErrorThenShowExportErrorMessage() {
        val directory = "testDirectory"
        val fileName = "testFileName"
        exportLiveData.postValue(ExportError(directory, fileName))

        assertNotDisplayed(R.id.note_export_loading)
        assertEnabled(R.id.note_export_start)
        assertTextInSnackbar(activity.getString(R.string.notes_export_error, directory, fileName))
    }

    @Test
    fun whenExportSuccessThenShowExportSuccessMessage() {
        val directory = "testDirectory"
        val fileName = "testFileName"
        exportLiveData.postValue(ExportSuccess(directory, fileName))

        assertNotDisplayed(R.id.note_export_loading)
        assertEnabled(R.id.note_export_start)
        assertTextInSnackbar(activity.getString(R.string.notes_export_success, directory, fileName))
    }

}