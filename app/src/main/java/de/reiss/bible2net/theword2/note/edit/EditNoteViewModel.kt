package de.reiss.bible2net.theword2.note.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword2.architecture.AsyncLoad
import de.reiss.bible2net.theword2.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword2.model.Note
import de.reiss.bible2net.theword2.model.TheWordContent
import java.util.Date

open class EditNoteViewModel(private val repository: EditNoteRepository) : ViewModel() {

    private val loadNoteLiveData: MutableLiveData<AsyncLoad<Note?>> = MutableLiveData()
    private val storeNoteLiveData: MutableLiveData<AsyncLoad<Void>> = MutableLiveData()

    open fun loadNoteLiveData() = loadNoteLiveData
    open fun storeNoteLiveData() = storeNoteLiveData

    open fun loadNote(date: Date) {
        repository.loadNote(date, loadNoteLiveData())
    }

    open fun storeNote(date: Date, text: String, theWordContent: TheWordContent) {
        repository.updateNote(date, text, theWordContent, storeNoteLiveData())
    }

    fun note() = loadNoteLiveData().value?.data

    fun isLoadingOrStoring() = loadNoteLiveData().value?.loadStatus == AsyncLoadStatus.LOADING ||
        storeNoteLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    fun loadError() = loadNoteLiveData().value?.loadStatus == AsyncLoadStatus.ERROR

    fun storeError() = storeNoteLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun storeSuccess() = storeNoteLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    fun onStoreErrorShown() {
        if (storeError()) {
            storeNoteLiveData().postValue(null)
        }
    }

    class Factory(private val repository: EditNoteRepository) :
        ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return EditNoteViewModel(repository) as T
        }
    }
}
