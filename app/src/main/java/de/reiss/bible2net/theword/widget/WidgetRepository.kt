package de.reiss.bible2net.theword.widget

import android.support.annotation.WorkerThread
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.TheWordItemDao
import de.reiss.bible2net.theword.database.converter.Converter
import de.reiss.bible2net.theword.model.TheWord
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

class WidgetRepository @Inject constructor(@Named("widget") private val executor: Executor,
                                           private val theWordItemDao: TheWordItemDao,
                                           private val bibleItemDao: BibleItemDao,
                                           private val appPreferences: AppPreferences) {

    fun loadWordForToday(onWordLoaded: (TheWord?) -> Unit) {
        executor.execute {
            onWordLoaded(findTheWord(Date().withZeroDayTime()))
        }
    }

    @WorkerThread
    private fun findTheWord(date: Date): TheWord? =
            appPreferences.chosenBible?.let { chosenBible ->
                bibleItemDao.find(chosenBible)?.let { bibleItem ->
                    theWordItemDao.byDate(bibleItem.id, date)?.let { theWordItem ->
                        return Converter.theWordItemToTheWord(bibleItem.bible, theWordItem)
                    }
                }
            }

}