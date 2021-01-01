package de.reiss.bible2net.theword.downloader.list

import de.reiss.bible2net.theword.logger.logWarn
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class ListDownloader(private val twdService: TwdService) {

    open fun downloadList(): List<Twd11>? {
        try {
            val response = twdService.list().execute()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            logWarn(e) { "Error when trying to load json list" }
        }
        logWarn { "Could not download json list successfully" }
        return null
    }

    open fun downloadListAsync(callback: DownloadListCallback) {

        twdService.list().enqueue(
            object : Callback<List<Twd11>> {

                override fun onResponse(
                    call: Call<List<Twd11>>,
                    response: Response<List<Twd11>>?
                ) {
                    if (response?.isSuccessful == true) {
                        response.body()?.let {
                            callback.onListDownloadFinished(success = true, data = it)
                            return
                        }
                    }
                    callback.onListDownloadFinished(success = false)
                }

                override fun onFailure(call: Call<List<Twd11>>, throwable: Throwable) {
                    callback.onListDownloadFinished(success = false)
                }
            }
        )
    }
}
