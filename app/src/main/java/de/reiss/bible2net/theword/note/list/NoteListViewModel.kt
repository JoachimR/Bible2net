package de.reiss.bible2net.theword.note.list

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus.LOADING
import de.reiss.bible2net.theword.model.Note

open class NoteListViewModel(private val repository: NoteListRepository) : ViewModel() {

    private val notesLiveData: MutableLiveData<AsyncLoad<List<Note>>> = MutableLiveData()

    open fun notesLiveData() = notesLiveData

    open fun loadNotes() {
        repository.getAllNotes(notesLiveData())
    }

    fun notes() = notesLiveData().value?.data ?: emptyList()

    fun isLoadingNotes() = notesLiveData().value?.loadStatus == LOADING

    class Factory(private val repository: NoteListRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return NoteListViewModel(repository) as T
        }

    }

}