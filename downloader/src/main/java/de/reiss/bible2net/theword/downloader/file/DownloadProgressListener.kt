package de.reiss.bible2net.theword.downloader.file

interface DownloadProgressListener {

    fun onUpdateProgress(url: String,
                         readBytes: Long,
                         allBytes: Long)

    fun onError(url: String,
                message: String? = null)

    fun onFinished(url: String,
                   twdData: String)

}