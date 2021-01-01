package de.reiss.bible2net.theword.bible

import androidx.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.main.BibleListUpdater
import de.reiss.bible2net.theword.model.Bible
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
