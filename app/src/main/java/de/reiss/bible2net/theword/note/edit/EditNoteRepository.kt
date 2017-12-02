package de.reiss.bible2net.theword.note.edit

import android.arch.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.note.loadNoteFromDatabase
import de.reiss.bible2net.theword.note.updateNoteInDatabase
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class EditNoteRepository @Inject constructor(private val executor: Executor,
                                                  private val noteItemDao: NoteItemDao) {

    open fun loadNote(date: Date, result: MutableLiveData<AsyncLoad<Note>>) {
        loadNoteFromDatabase(date, executor, noteItemDao, result)
    }

    open fun updateNote(date: Date,
                        text: String,
                        theWordContent: TheWordContent,
                        result: MutableLiveData<AsyncLoad<Void>>) {
        updateNoteInDatabase(executor, noteItemDao, date, text, theWordContent, result)
    }

}