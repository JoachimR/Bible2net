package de.reiss.bible2net.theword.preferences

import android.arch.lifecycle.MutableLiveData
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.model.Bible
import java.util.concurrent.Executor
import javax.inject.Inject

class AppPreferencesRepository @Inject constructor(private val executor: Executor,
                                                   private val bibleItemDao: BibleItemDao) {

    fun loadBibleItems(result: MutableLiveData<AsyncLoad<List<Bible>>>) {
        val oldData = result.value?.data
        result.postValue(AsyncLoad.loading(oldData))

        executor.execute {
            val allData = bibleItemDao.all().map {
                Bible(it.bible, it.name, it.languageCode)
            }
            result.postValue(AsyncLoad.success(allData))
        }
    }

}