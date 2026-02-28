package de.reiss.bible2net.theword2.main

import androidx.annotation.WorkerThread
import de.reiss.bible2net.theword2.database.BibleItem
import de.reiss.bible2net.theword2.database.BibleItemDao
import de.reiss.bible2net.theword2.downloader.list.ListDownloader
import de.reiss.bible2net.theword2.downloader.list.Twd11
import de.reiss.bible2net.theword2.preferences.AppPreferences
import javax.inject.Inject

open class BibleListUpdater @Inject constructor(
    private val listDownloader: ListDownloader,
    private val bibleItemDao: BibleItemDao,
    private val appPreferences: AppPreferences
) {

    @WorkerThread
    open fun tryUpdateBiblesIfNeeded() {
        if (appPreferences.biblesNeedUpdate()) {
            listDownloader.downloadList()?.let {
                updateBibleItems(it)
                appPreferences.lastTimeBiblesUpdated = System.currentTimeMillis()
            }
        }
    }

    private fun updateBibleItems(jsonList: List<Twd11>) {
        val validBibles = jsonList
            .filter { it.bible.isNotEmpty() && it.bibleName.isNotEmpty() }

        validBibles
            .map { BibleItem(it.bible, it.bibleName, it.language) }
            .forEach { bibleItem ->
                if (bibleItemDao.find(bibleItem.bible) == null) {
                    bibleItemDao.insert(bibleItem)
                }
            }

        val apiBibleKeys = validBibles.map { it.bible }.toSet()
        bibleItemDao.all()
            .filter { it.bible !in apiBibleKeys }
            .forEach { bibleItemDao.delete(it) }
    }
}
