package de.reiss.bible2net.theword.downloader.file

import okhttp3.ResponseBody
import okio.*
import java.io.IOException

class ProgressResponseBody(private val url: String,
                           private val responseBody: ResponseBody,
                           private val progressListener: DownloadProgressListener) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType() = responseBody.contentType()

    override fun contentLength() = responseBody.contentLength()

    override fun source(): BufferedSource {
        bufferedSource.let { currentSource ->
            if (currentSource == null) {
                val bufferedSource = Okio.buffer(source(responseBody.source()))
                this.bufferedSource = bufferedSource
                return bufferedSource
            } else {
                return currentSource
            }
        }
    }

    private fun source(source: Source) = object : ForwardingSource(source) {

        private var totalBytesRead = 0L

        @Throws(IOException::class)
        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)

            // read() returns the number of bytes read, or -1 if this source is exhausted.
            totalBytesRead += if (bytesRead != -1L) bytesRead else 0

            progressListener.onUpdateProgress(
                    url = url,
                    readBytes = totalBytesRead,
                    allBytes = responseBody.contentLength())

            return bytesRead
        }
    }

}