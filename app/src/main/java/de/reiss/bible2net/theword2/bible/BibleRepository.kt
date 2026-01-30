package de.reiss.bible2net.theword2.bible

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword2.architecture.AsyncLoad
import de.reiss.bible2net.theword2.database.BibleItemDao
import de.reiss.bible2net.theword2.main.BibleListUpdater
import de.reiss.bible2net.theword2.model.Bible
import java.util.concurrent.Executor
import javax.inject.Inject

class BibleRepository @Inject constructor(
    private val executor: Executor,
    private val bibleListUpdater: BibleListUpdater,
    private val bibleItemDao: BibleItemDao
) {

    fun updateBibles(result: MutableLiveData<AsyncLoad<List<Bible>>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            bibleListUpdater.tryUpdateBiblesIfNeeded()

            val allData = bibleItemDao.all().map {
                Bible(it.bible, it.name, it.languageCode)
            }
            result.postValue(AsyncLoad.success(allData))
        }
    }
}
