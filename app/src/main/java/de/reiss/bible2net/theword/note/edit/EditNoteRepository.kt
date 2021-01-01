package de.reiss.bible2net.theword.note.edit

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItem
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.logger.logWarn
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.Date
import java.util.concurrent.Executor
import javax.inject.Inject

open class EditNoteRepository @Inject constructor(
    private val executor: Executor,
    private val noteItemDao: NoteItemDao
) {

    open fun loadNote(date: Date, result: MutableLiveData<AsyncLoad<Note?>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            try {
                val noteItem = noteItemDao.byDate(date.withZeroDayTime())
                val note = Converter.noteItemToNote(noteItem) // can be null (new note)
                result.postValue(AsyncLoad.success(note))
            } catch (e: Exception) {
                logWarn(e) { "Error while loading note" }
                result.postValue(AsyncLoad.error(oldData))
            }
        }
    }

    open fun updateNote(
        date: Date,
        text: String,
        theWordContent: TheWordContent,
        result: MutableLiveData<AsyncLoad<Void>>
    ) {
        result.postValue(AsyncLoad.loading())
        executor.execute {
            try {
                if (text.trim().isEmpty()) {
                    noteItemDao.byDate(date.withZeroDayTime())?.let { noteItem ->
                        noteItemDao.delete(noteItem)
                    }
                } else {
                    noteItemDao.insertOrReplace(
                        NoteItem(date.withZeroDayTime(), theWordContent, text)
                    )
                }
                result.postValue(AsyncLoad.success())
            } catch (e: Exception) {
                logWarn(e) { "Error while trying to update note" }
                result.postValue(AsyncLoad.error())
            }
        }
    }
}
