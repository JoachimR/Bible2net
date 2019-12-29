package de.reiss.bible2net.theword.main.content

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.architecture.AsyncLoadStatus
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWord
import java.util.*

open class TheWordViewModel(private val repository: TheWordRepository) : ViewModel() {

    private val theWordLiveData: MutableLiveData<AsyncLoad<TheWord>> = MutableLiveData()
    private val noteLiveData: MutableLiveData<AsyncLoad<Note>> = MutableLiveData()

    open fun theWordLiveData() = theWordLiveData
    open fun noteLiveData() = noteLiveData

    open fun loadTheWord(date: Date) {
        repository.getTheWordFor(date, theWordLiveData())
    }

    open fun loadNote(date: Date) {
        repository.getNoteFor(date, noteLiveData())
    }

    fun theWord() = theWordLiveData().value?.data
    fun note() = noteLiveData().value?.data

    fun isLoadingTheWord() = theWordLiveData().value?.loadStatus == AsyncLoadStatus.LOADING
    fun isErrorForTheWord() = theWordLiveData().value?.loadStatus == AsyncLoadStatus.ERROR
    fun isSuccessForTheWord() = theWordLiveData().value?.loadStatus == AsyncLoadStatus.SUCCESS

    fun isLoadingNote() = noteLiveData().value?.loadStatus == AsyncLoadStatus.LOADING

    class Factory(private val repository: TheWordRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            @Suppress("UNCHECKED_CAST")
            return TheWordViewModel(repository) as T
        }

    }

}