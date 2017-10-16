package de.reiss.bible2net.theword.main.viewpager

import android.arch.lifecycle.MutableLiveData
import android.support.annotation.WorkerThread
import de.reiss.bible2net.theword.architecture.AsyncLoad
import de.reiss.bible2net.theword.database.BibleItemDao
import de.reiss.bible2net.theword.database.TheWordItem
import de.reiss.bible2net.theword.database.TheWordItemDao
import de.reiss.bible2net.theword.downloader.file.DownloadProgressListener
import de.reiss.bible2net.theword.downloader.file.FileDownloader
import de.reiss.bible2net.theword.downloader.list.ListDownloader
import de.reiss.bible2net.theword.downloader.list.Twd11
import de.reiss.bible2net.theword.logger.logInfo
import de.reiss.bible2net.theword.logger.logWarn
import de.reiss.bible2net.theword.logger.logWarnWithCrashlytics
import de.reiss.bible2net.theword.twdparser.TwdItem
import de.reiss.bible2net.theword.twdparser.TwdParser
import de.reiss.bible2net.theword.util.extensions.amountOfDaysInRange
import de.reiss.bible2net.theword.util.extensions.asDateString
import de.reiss.bible2net.theword.util.extensions.extractYear
import de.reiss.bible2net.theword.util.extensions.withZeroDayTime
import de.reiss.bible2net.theword.widget.WidgetProvider
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

open class ViewPagerRepository @Inject constructor(private val executor: Executor,
                                                   private val listDownloader: ListDownloader,
                                                   private val fileDownloader: FileDownloader,
                                                   private val theWordItemDao: TheWordItemDao,
                                                   private val bibleItemDao: BibleItemDao) {

    open fun getItemsFor(bible: String,
                         fromDate: Date,
                         toDate: Date,
                         result: MutableLiveData<AsyncLoad<String>>) {

        // set value instead of post value on purpose
        // otherwise the fragment might invoke this twice
        result.value = AsyncLoad.loading(bible)

        executor.execute {

            val storedItems = readStoredItems(bible, fromDate, toDate)
            val expectedAmountOfDays = fromDate.amountOfDaysInRange(toDate)

            if (storedItems == null || storedItems.size < expectedAmountOfDays) {
                logWarn {
                    "Not enough items found (actual:${storedItems?.size ?: 0}, " +
                            "expected:$expectedAmountOfDays) for bible '$bible' in date range: " +
                            fromDate.asDateString() + " - " + toDate.asDateString() +
                            ". Will try to download and store items"
                }

                val databaseUpdated = downloadAndStoreItems(bible, fromDate, toDate)

                // always return success, we only tried update
                result.postValue(AsyncLoad.success(bible))

                if (databaseUpdated) {
                    WidgetProvider.refreshWidgets()
                }
            } else {
                result.postValue(AsyncLoad.success(bible))
            }
        }
    }

    private fun readStoredItems(bible: String,
                                fromDate: Date,
                                toDate: Date): List<TheWordItem>? {
        return bibleItemDao.find(bible)?.id?.let { bibleId ->
            getForRange(bibleId, fromDate, toDate)
        }
    }

    @WorkerThread
    private fun downloadAndStoreItems(bible: String,
                                      fromDate: Date,
                                      toDate: Date): Boolean {

        listDownloader.downloadList()?.let { jsonList ->
            return downloadAndStore(
                    bible = bible,
                    fromYear = fromDate.extractYear(),
                    toYear = toDate.extractYear(),
                    jsonList = jsonList
            )
        }
        return false
    }

    private fun downloadAndStore(bible: String,
                                 fromYear: Int,
                                 toYear: Int,
                                 jsonList: List<Twd11>): Boolean {

        var updated = false

        val allTwdUrls = jsonList
                .filter {
                    it.bible == bible && (it.year == fromYear || it.year == toYear)
                }
                .map {
                    it.twdFileUrl
                }


        if (allTwdUrls.isNotEmpty()) {

            val listener: DownloadProgressListener = object : DownloadProgressListener {

                override fun onUpdateProgress(url: String,
                                              readBytes: Long, allBytes: Long) {
                    logInfo { "downloading $url ... ${(readBytes * 100 / allBytes).toInt()}%" }
                }

                override fun onError(url: String,
                                     message: String?) {
                    logWarnWithCrashlytics { "$url download error" }
                }

                override fun onFinished(url: String,
                                        twdData: String) {
                    logInfo { "$url downloaded successfully" }

                    if (writeTwdToDatabase(twdData)) {
                        updated = true
                    }
                }
            }

            for (url in allTwdUrls) {
                fileDownloader.download(url, listener)
            }
        }

        return updated
    }

    @WorkerThread
    fun writeTwdToDatabase(twdData: String): Boolean {

        val parsed = TwdParser.parse(twdData)

        val twd = parsed.first()
        val bibleItem = bibleItemDao.find(twd.bible)
                ?: throw IllegalStateException("Bible ${twd.bible} not found in database")

        val items = parsed.map {
            asDatabaseItem(bibleItem.id, it)
        }.toTypedArray()

        val inserted = theWordItemDao.insertOrReplace(*items)
        return inserted.isNotEmpty()
    }

    private fun getForRange(bibleId: Int, fromDate: Date, toDate: Date) =
            theWordItemDao.range(bibleId, fromDate.withZeroDayTime(), toDate.withZeroDayTime())

    private fun asDatabaseItem(bibleId: Int, twd: TwdItem) =
            TheWordItem().apply {
                this.bibleId = bibleId
                this.date = twd.date.withZeroDayTime()
                this.book1 = twd.parol1.book
                this.chapter1 = twd.parol1.chapter
                this.verse1 = twd.parol1.verse
                this.id1 = twd.parol1.id
                this.intro1 = twd.parol1.intro
                this.text1 = twd.parol1.text
                this.ref1 = twd.parol1.ref
                this.book2 = twd.parol2.book
                this.chapter2 = twd.parol2.chapter
                this.verse2 = twd.parol2.verse
                this.id2 = twd.parol2.id
                this.intro2 = twd.parol2.intro
                this.text2 = twd.parol2.text
                this.ref2 = twd.parol2.ref
            }
}