package de.reiss.bible2net.theword.note.list

import android.arch.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.model.Note
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteListRepository @Inject constructor(private val executor: Executor,
                                                  private val noteItemDao: NoteItemDao) {

    open fun getAllNotes(result: MutableLiveData<AsyncLoad<List<Note>>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val allData = noteItemDao.all().mapNotNull {
                Converter.noteItemToNote(it)
            }
            result.postValue(AsyncLoad.success(allData))
        }
    }

}