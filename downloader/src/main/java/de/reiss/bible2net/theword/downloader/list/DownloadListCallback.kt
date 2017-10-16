package de.reiss.bible2net.theword.downloader.list

interface DownloadListCallback {

    fun onListDownloadFinished(success: Boolean, data: List<Twd11>? = null)

}