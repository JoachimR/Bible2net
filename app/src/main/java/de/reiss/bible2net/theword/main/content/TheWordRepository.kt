package de.reiss.bible2net.theword.main.content

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.database.TheWordItem
import de.reiss.bible2net.theword.database.TheWordItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.main.BibleListUpdater
import de.reiss.bible2net.theword.model.Note
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.model.TheWordContent
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class TheWordRepository @Inject constructor(private val executor: Executor,
                                                 private val bibleListUpdater: BibleListUpdater,
                                                 private val theWordItemDao: TheWordItemDao,
                                                 private val bibleItemDao: BibleItemDao,
                                                 private val noteItemDao: NoteItemDao,
                                                 private val appPreferences: AppPreferences) {

    open fun getTheWordFor(date: Date, result: MutableLiveData<AsyncLoad<TheWord>>) {

        val oldData = result.value?.data
        result.value = AsyncLoad.loading(oldData)

        executor.execute {
            bibleListUpdater.tryUpdateBiblesIfNeeded()

            val bibleItem = bibleItemDao.find(appPreferences.chosenBible)
            if (bibleItem == null) {
                result.postValue(AsyncLoad.error(message = "Bible not found"))
            } else {
                val fromDatabase = theWordItemDao.byDate(bibleItem.id, date.withZeroDayTime())
                if (fromDatabase == null) {
                    result.postValue(AsyncLoad.error(message = "Content not found"))
                } else {
                    result.postValue(AsyncLoad.success(dbItemToTheWord(bibleItem.bible, fromDatabase)))
                }
            }

        }
    }

    open fun getNoteFor(date: Date, result: MutableLiveData<AsyncLoad<Note>>) {

        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val noteItem = noteItemDao.byDate(date.withZeroDayTime())
            result.postValue(AsyncLoad.success(Converter.noteItemToNote(noteItem)))
        }
    }

    private fun dbItemToTheWord(bible: String, twd: TheWordItem) = TheWord(
            bible = bible,
            date = twd.date,
            content = TheWordContent(twd.book1, twd.chapter1, twd.verse1, twd.id1,
                    twd.intro1, twd.text1, twd.ref1,
                    twd.book2, twd.chapter2, twd.verse2, twd.id2,
                    twd.intro2, twd.text2, twd.ref2)
    )

}