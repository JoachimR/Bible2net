package de.reiss.bible2net.theword.note.export

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

open class NoteExportViewModel(private val repository: NoteExportRepository) : ViewModel() {

    private val exportLiveData: MutableLiveData<NoteExportStatus> = MutableLiveData()

    open fun exportLiveData() = exportLiveData

    open fun exportNotes() {
        repository.exportNotes(exportLiveData())
    }

    fun isExporting() = exportLiveData().value is ExportingStatus

    class Factory(private val repository: NoteExportRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return NoteExportViewModel(repository) as T
        }

    }

    fun clearLiveData() {
        if (isExporting().not()) {
            exportLiveData().postValue(null)
        }
    }

}