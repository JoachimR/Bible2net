package de.reiss.bible2net.theword2.note.export

import android.content.Context
import android.os.Environment
import de.reiss.bible2net.theword2.logger.logWarn
import de.reiss.bible2net.theword2.util.extensions.asDateString
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date

open class FileProvider(private val context: Context) {

    val fileName = "${Date().asDateString()}_wordnotes.xml"

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
        val absoluteDir = context.getExternalFilesDir(null)!!.absoluteFile.toString()
        val dir = File(absoluteDir)
        dir.mkdirs()
        val file = File(dir, fileName)
        file.createNewFile()
        return file
    }
}
