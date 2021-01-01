package de.reiss.bible2net.theword.note.details

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.logger.logWarn
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteDetailsRepository @Inject constructor(
    private val executor: Executor,
    private val noteItemDao: NoteItemDao
) {

    open fun loadNote(note: Note, result: MutableLiveData<AsyncLoad<Note>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            try {
                val noteItem = noteItemDao.byDate(note.date.withZeroDayTime())
                val loadedNote = Converter.noteItemToNote(noteItem)

                if (loadedNote == null) {
                    logWarn { "note not found" }
                    result.postValue(AsyncLoad.error(oldData))
                } else {
                    result.postValue(AsyncLoad.success(loadedNote))
                }
            } catch (e: Exception) {
                logWarn(e) { "Error while loading note" }
                result.postValue(AsyncLoad.error(oldData))
            }
        }
    }

    open fun deleteNote(
        note: Note,
        result: MutableLiveData<AsyncLoad<Void>>
    ) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))
        executor.execute {
            val noteItem = noteItemDao.byDate(note.date.withZeroDayTime())
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
}
