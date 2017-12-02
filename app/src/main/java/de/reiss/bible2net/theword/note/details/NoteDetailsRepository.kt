package de.reiss.bible2net.theword.note.details

import android.arch.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.note.deleteNoteFromDatabase
import de.reiss.bible2net.theword.note.loadNoteFromDatabase
import java.util.concurrent.Executor
import javax.inject.Inject

open class NoteDetailsRepository @Inject constructor(private val executor: Executor,
                                                     private val noteItemDao: NoteItemDao) {

    open fun loadNote(note: Note, result: MutableLiveData<AsyncLoad<Note>>) {
        loadNoteFromDatabase(note.date, executor, noteItemDao, result)
    }

    open fun deleteNote(note: Note,
                        result: MutableLiveData<AsyncLoad<Void>>) {
        deleteNoteFromDatabase(executor, noteItemDao, note.date, result)
    }

}