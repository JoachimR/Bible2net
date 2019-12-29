package de.reiss.bible2net.theword.downloader.file

import android.webkit.URLUtil
import de.reiss.bible2net.theword.logger.logWarnWithCrashlytics
import okhttp3.OkHttpClient
import okhttp3.Request

open class FileDownloader(private val okHttpClient: OkHttpClient) {

    open fun download(url: String,
                      listener: DownloadProgressListener) {
        if (URLUtil.isValidUrl(url).not()) {
            listener.onError(url, "invalid url")
            return
        }

        try {
            okHttpClient.newBuilder().build()
                    .newCall(Request.Builder().url(url).build())
                    .execute().let { response ->

                        if (response.isSuccessful) {
                            response.body.let { body ->
                                if (body == null) {
                                    listener.onError(url, "downloaded body is empty")
                                } else {
                                    listener.onFinished(url, body.string())
                                }
                            }
                        } else {
                            listener.onError(url, "something went wrong during download")
                        }
            }
        } catch (e: Exception) {
            logWarnWithCrashlytics(e) { "error when trying to download $url" }
            listener.onError(url, "error when downloading")
        }
    }

}