package de.reiss.bible2net.theword.note.export

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

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