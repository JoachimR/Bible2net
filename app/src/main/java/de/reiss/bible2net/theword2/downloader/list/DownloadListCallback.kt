package de.reiss.bible2net.theword2.downloader.list

interface DownloadListCallback {

    fun onListDownloadFinished(success: Boolean, data: List<Twd11>? = null)
}
