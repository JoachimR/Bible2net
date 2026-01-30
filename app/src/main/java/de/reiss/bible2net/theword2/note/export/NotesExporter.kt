package de.reiss.bible2net.theword2.note.export

import androidx.annotation.WorkerThread
import de.reiss.bible2net.theword2.database.NoteItem
import de.reiss.bible2net.theword2.logger.logError
import de.reiss.bible2net.theword2.util.extensions.asDateString
import java.io.BufferedOutputStream
import java.io.IOException

open class NotesExporter(private val fileProvider: FileProvider) {

    open val fileName = fileProvider.fileName

    @WorkerThread
    open fun exportNotes(notes: List<NoteItem>): Boolean {
        val bos = fileProvider.createBufferedOutputStream() ?: return false
        val exporter = Exporter(bos)
        try {
            doExport(exporter, notes)
            return true
        } catch (e: IOException) {
            logError(e) { "Error when trying to export notes database" }
        } finally {
            try {
                exporter.close()
            } catch (ignored: IOException) {
            }
        }
        return false
    }

    open fun isExternalStorageWritable() = fileProvider.isExternalStorageWritable()

    private fun doExport(exporter: Exporter, allNotes: List<NoteItem>) {
        exporter.apply {
            startDbExport("NotesDatabase")
            startTable("notes")

            for (note in allNotes) {
                startRow()
                addColumn("date", note.date.asDateString())

                addColumn("book1", note.book1)
                addColumn("chapter1", note.chapter1)
                addColumn("verse1", note.verse1)
                addColumn("intro1", note.intro1)
                addColumn("text1", note.text1)
                addColumn("ref1", note.ref1)

                addColumn("book2", note.book2)
                addColumn("chapter2", note.chapter2)
                addColumn("verse2", note.verse2)
                addColumn("intro2", note.intro2)
                addColumn("text2", note.text2)
                addColumn("ref2", note.ref2)

                addColumn("note", note.note)
                endRow()
            }
            endTable()
            endDbExport()
        }
    }

    private class Exporter(private val bufferedOutputStream: BufferedOutputStream) {

        companion object {

            private const val CLOSING_WITH_TICK = "'>"
            private const val START_DB = "\n<export-database name='"
            private const val END_DB = "\n</export-database>"
            private const val START_TABLE = "\n\n\n<table name='"
            private const val END_TABLE = "\n</table>"
            private const val START_ROW = "\n\n<row>"
            private const val END_ROW = "\n</row>"
            private const val START_COL = "\n<col name='"
            private const val END_COL = "</col>"
        }

        @Throws(IOException::class)
        fun close() {
            bufferedOutputStream.close()
        }

        @Throws(IOException::class)
        fun startDbExport(dbName: String) {
            val stg = START_DB + dbName + CLOSING_WITH_TICK
            bufferedOutputStream.write(stg.toByteArray())
        }

        @Throws(IOException::class)
        fun endDbExport() {
            bufferedOutputStream.write(END_DB.toByteArray())
        }

        @Throws(IOException::class)
        fun startTable(tableName: String) {
            val stg = START_TABLE + tableName + CLOSING_WITH_TICK
            bufferedOutputStream.write(stg.toByteArray())
        }

        @Throws(IOException::class)
        fun endTable() {
            bufferedOutputStream.write(END_TABLE.toByteArray())
        }

        @Throws(IOException::class)
        fun startRow() {
            bufferedOutputStream.write(START_ROW.toByteArray())
        }

        @Throws(IOException::class)
        fun endRow() {
            bufferedOutputStream.write(END_ROW.toByteArray())
        }

        @Throws(IOException::class)
        fun addColumn(name: String, value: String) {
            val stg = START_COL + name + CLOSING_WITH_TICK + value + END_COL
            bufferedOutputStream.write(stg.toByteArray())
        }
    }
}
