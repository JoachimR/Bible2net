package de.reiss.bible2net.theword.note.export

import android.os.Environment
import de.reiss.bible2net.theword.logger.logWarn
import de.reiss.bible2net.theword.util.extensions.asDateString
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

open class FileProvider {

    private val directoryName = "WordsNotes"

    val fileName = "${Date().asDateString()}_wordnotes.xml"

    open val directory by lazy {
        Environment.getExternalStorageDirectory().toString() +
                File.separator + directoryName
    }

    open fun isExternalStorageWritable() =
            Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    open fun createBufferedOutputStream(): BufferedOutputStream? {
        try {
            return BufferedOutputStream(FileOutputStream(createFile()))
        } catch (e: IOException) {
            logWarn(e) { "error when creating file" }
        } catch (e: SecurityException) {
            logWarn(e) { "security error when creating file" }
        }
        return null
    }

    private fun createFile(): File {
        val dir = File(directory)
        dir.mkdirs()
        val file = File(dir, fileName)
        file.createNewFile()
        return file
    }

}