package de.reiss.bible2net.theword.note

import android.arch.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItem
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.logger.logWarn
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor

fun loadNoteFromDatabase(date: Date,
                         executor: Executor,
                         noteItemDao: NoteItemDao,
                         result: MutableLiveData<AsyncLoad<Note>>) {

    val oldData = result.value?.data
    result.postValue(AsyncLoad.loading(oldData))

    executor.execute {
        try {
            val noteItem = noteItemDao.byDate(date.withZeroDayTime())
            val note = Converter.noteItemToNote(noteItem)
            result.postValue(AsyncLoad.success(note))
        } catch (e: Exception) {
            logWarn(e) { "Error while loading note" }
            result.postValue(AsyncLoad.error(oldData))
        }
    }
}

fun updateNoteInDatabase(executor: Executor,
                         noteItemDao: NoteItemDao,
                         date: Date,
                         text: String,
                         theWordContent: TheWordContent,
                         result: MutableLiveData<AsyncLoad<Void>>) {

    result.postValue(AsyncLoad.loading())

    executor.execute {
        try {
            noteItemDao.insertOrReplace(NoteItem(date.withZeroDayTime(), theWordContent, text))
            result.postValue(AsyncLoad.success())
        } catch (e: Exception) {
            logWarn(e) { "Error while trying to update note" }
            result.postValue(AsyncLoad.error())
        }
    }
}

fun deleteNoteFromDatabase(executor: Executor,
                           noteItemDao: NoteItemDao,
                           date: Date,
                           result: MutableLiveData<AsyncLoad<Void>>) {
    val oldData = result.value?.data
    result.postValue(AsyncLoad.loading(oldData))

    executor.execute {
        val noteItem = noteItemDao.byDate(date.withZeroDayTime())
        val deletedRows = if (noteItem == null) 0 else noteItemDao.delete(noteItem)
        result.postValue(
                if (deletedRows > 0) {
                    AsyncLoad.success()
                } else {
                    AsyncLoad.error()
                }
        )
    }
}