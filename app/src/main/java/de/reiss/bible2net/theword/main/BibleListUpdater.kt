package de.reiss.bible2net.theword.main

import androidx.annotation.WorkerThread
import de.reiss.bible2net.theword.database.BibleItem
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.downloader.list.ListDownloader
import de.reiss.bible2net.theword.downloader.list.Twd11
import de.reiss.bible2net.theword.preferences.AppPreferences
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
        jsonList
            .filter { jsonItem ->
                jsonItem.bible.isNotEmpty() && jsonItem.bibleName.isNotEmpty()
            }
            .map { jsonItem ->
                BibleItem(jsonItem.bible, jsonItem.bibleName, jsonItem.language)
            }
            .forEach { bibleItem ->
                if (bibleItemDao.find(bibleItem.bible) == null) {
                    bibleItemDao.insert(bibleItem)
                }
            }
    }
}
