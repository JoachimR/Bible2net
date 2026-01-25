package de.reiss.bible2net.theword.note.export

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword.App
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.architecture.AppFragment
import de.reiss.bible2net.theword.databinding.NoteExportFragmentBinding
import de.reiss.bible2net.theword.note.export.NoteExportStatus.ExportError
import de.reiss.bible2net.theword.note.export.NoteExportStatus.ExportSuccess
import de.reiss.bible2net.theword.note.export.NoteExportStatus.Exporting
import de.reiss.bible2net.theword.note.export.NoteExportStatus.NoNotes
import de.reiss.bible2net.theword.note.export.NoteExportStatus.NoPermission
import de.reiss.bible2net.theword.util.extensions.onClick
import de.reiss.bible2net.theword.util.extensions.showShortSnackbar

class NoteExportFragment : AppFragment<NoteExportFragmentBinding, NoteExportViewModel>(
    R.layout.note_export_fragment
) {

    companion object {
        fun createInstance() = NoteExportFragment()
    }

    override fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        NoteExportFragmentBinding.inflate(inflater, container, false)

    override fun initViews() {
        binding.noteExportStart.onClick {
            tryExportNotes()
        }
    }

    override fun defineViewModelProvider(): ViewModelProvider =
        ViewModelProvider(
            this,
            NoteExportViewModel.Factory(
                App.component.noteExportRepository
            )
        )

    override fun defineViewModel(): NoteExportViewModel =
        loadViewModelProvider()[NoteExportViewModel::class.java]

    override fun initViewModelObservers() {
        viewModel.exportLiveData().observe(this) {
            updateUi()
        }
    }

    private fun tryExportNotes() {
        if (viewModel.isExporting().not()) {
            viewModel.exportNotes()
        }
    }

    private fun updateUi() {
        val status = viewModel.exportLiveData().value ?: return

        val isExporting = status is Exporting
        updateLoading(isExporting)

        if (isExporting.not()) {
            showShortSnackbar(
                message = messageFor(status),
                callback = {
                    viewModel.clearLiveData()
                }
            )
        }
    }

    private fun messageFor(status: NoteExportStatus) =
        when (status) {
            NoPermission -> getString(R.string.can_not_write_to_sdcard)
            NoNotes -> getString(R.string.notes_export_no_notes)
            is ExportError -> getString(R.string.notes_export_error, status.fileName)
            is ExportSuccess -> getString(R.string.notes_export_success, status.fileName)
            Exporting -> throw IllegalStateException("invalid status")
        }

    private fun updateLoading(loading: Boolean) {
        binding.noteExportLoading.setLoading(loading)
        binding.noteExportStart.isEnabled = !loading
    }
}
