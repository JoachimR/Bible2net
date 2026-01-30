package de.reiss.bible2net.theword2.note.export

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword2.note.export.NoteExportStatus.Exporting

open class NoteExportViewModel(private val repository: NoteExportRepository) : ViewModel() {

    private val exportLiveData: MutableLiveData<NoteExportStatus> = MutableLiveData()

    open fun exportLiveData() = exportLiveData

    open fun exportNotes() {
        repository.exportNotes(exportLiveData())
    }

    fun isExporting() = exportLiveData().value is Exporting

    class Factory(private val repository: NoteExportRepository) :
        ViewModelProvider.NewInstanceFactory() {

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
