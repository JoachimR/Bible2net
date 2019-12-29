package de.reiss.bible2net.theword.migration

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Color
import androidx.annotation.WorkerThread
import de.reiss.bible2net.theword.R
import de.reiss.bible2net.theword.database.NoteItem
import de.reiss.bible2net.theword.database.NoteItemDao
import de.reiss.bible2net.theword.logger.logErrorWithCrashlytics
import de.reiss.bible2net.theword.preferences.AppPreferences
import de.reiss.bible2net.theword.twdparser.dateFromString
import java.util.concurrent.Executor
import javax.inject.Inject

class MigrateTo127 @Inject constructor(val context: Context,
                                       val executor: Executor,
                                       val noteItemDao: NoteItemDao,
                                       val appPreferences: AppPreferences) {

    companion object {

        const val migrateKey = "PERFORMED_MIGRATION_TO_127";

    }

    fun migrateIfNeeded() {
        val preferences = appPreferences.preferences

        try {
            val alreadyMigrated = preferences.getBoolean(migrateKey, false)
            if (alreadyMigrated.not()) {
                preferences.edit().putBoolean(migrateKey, true).apply()

                migrateAppPreferences(preferences)
                migrateWidgetPreferences(preferences)
                cleanOldPreferences(preferences)
                migrateDatabases()
            }
        } catch (e: Exception) {
            logErrorWithCrashlytics(e) {
                "error when trying to migrate preferences to version app 126"
            }
        }
    }

    private fun migrateDatabases() {
        executor.execute {
            try {
                WordsDbCommunicator(context).deleteAll()
                migrateNotesDatabase()
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) {
                    "error when trying to delete or migrate old databases"
                }
            }
        }
    }

    @WorkerThread
    private fun migrateNotesDatabase() {
        val notesDbCommunicator = NotesDbCommunicator(context)
        val cursor = notesDbCommunicator.cursorForAll()
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                moveToNewDatabase(cursor)
            }
        }
    }

    private fun moveToNewDatabase(cursor: Cursor) {
        // find date of old note
        cursor.getString(cursor.getColumnIndex("date"))?.let { dateString ->
            dateFromString(dateString)?.let { date ->

                // only insert old note into new database if there is not already
                // a note in the new database for this date
                val item = noteItemDao.byDate(date)
                if (item == null) {
                    val noteItem = NoteItem(date,
                            cursor.getString(cursor.getColumnIndex("book1")),
                            cursor.getString(cursor.getColumnIndex("chapter1")),
                            cursor.getString(cursor.getColumnIndex("verse1")),
                            cursor.getString(cursor.getColumnIndex("id1")),
                            cursor.getString(cursor.getColumnIndex("intro1")),
                            cursor.getString(cursor.getColumnIndex("text1")),
                            cursor.getString(cursor.getColumnIndex("ref1")),
                            cursor.getString(cursor.getColumnIndex("book2")),
                            cursor.getString(cursor.getColumnIndex("chapter2")),
                            cursor.getString(cursor.getColumnIndex("verse2")),
                            cursor.getString(cursor.getColumnIndex("id2")),
                            cursor.getString(cursor.getColumnIndex("intro2")),
                            cursor.getString(cursor.getColumnIndex("text2")),
                            cursor.getString(cursor.getColumnIndex("ref2")),
                            cursor.getString(cursor.getColumnIndex("note")))
                    noteItemDao.insertOrReplace(noteItem)
                }
            }
        }
    }

    private fun cleanOldPreferences(preferences: SharedPreferences) {
        val edit = preferences.edit()
        preferences.all
                .map { it.key }
                .filter { keysAfterMigration.contains(it).not() }
                .forEach {
                    edit.remove(it)
                }
        edit.apply()
    }

    private fun migrateAppPreferences(preferences: SharedPreferences) {
        val edit = preferences.edit()

        preferences.getString("pref_theme_key", null)?.let {
            if (context.resources.getStringArray(R.array.pref_theme_themes_values).contains(it)) {
                edit.putString(context.getString(R.string.pref_theme_key), it)
            }
        }

        preferences.getBoolean("pref_shownotes_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_shownotes_key), it)
        }

        preferences.getString("pref_fontsize_key", null)?.let {
            try {
                edit.putInt(context.getString(R.string.pref_fontsize_key), Integer.parseInt(it))
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) { "error when trying to migrate app font size" }
            }
        }

        edit.apply()
    }

    private fun migrateWidgetPreferences(preferences: SharedPreferences) {
        val edit = preferences.edit()

        preferences.getString("pref_widget_fontsize_key", null)?.let {
            try {
                edit.putInt(context.getString(R.string.pref_widget_fontsize_key),
                        Integer.parseInt(it))
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) { "error when trying to migrate widget font size" }
            }
        }

        preferences.getString("pref_widget_fontcolor_key", null)?.let {
            try {
                val color = Color.parseColor(it)
                edit.putInt(context.getString(R.string.pref_widget_fontcolor_key), color)
            } catch (e: Exception) {
                logErrorWithCrashlytics(e) { "error when trying to migrate widget font color" }
            }
        }

        preferences.getString("pref_widget_backgroundcolor_key", null)?.let {
            edit.putString(context.getString(R.string.pref_widget_backgroundcolor_key),
                    if (it == "border_content_transparent") {
                        context.getString(R.string.pref_widget_backgroundcolor_default)
                    } else {
                        it
                    })
        }

        preferences.getBoolean("pref_widget_centered_text_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_widget_centered_text_key), it)
        }

        preferences.getBoolean("pref_widget_showdate_key", true).let {
            edit.putBoolean(context.getString(R.string.pref_widget_showdate_key), it)
        }

        edit.apply()
    }

    private val keysAfterMigration: List<String> by lazy {
        listOf<String>(
                migrateKey,
                context.getString(R.string.pref_theme_key),
                context.getString(R.string.pref_language_key),
                context.getString(R.string.pref_fontsize_key),
                context.getString(R.string.pref_shownotes_key),
                context.getString(R.string.pref_widget_backgroundcolor_key),
                context.getString(R.string.pref_widget_fontcolor_key),
                context.getString(R.string.pref_widget_fontsize_key),
                context.getString(R.string.pref_widget_showdate_key),
                context.getString(R.string.pref_widget_centered_text_key),
                context.getString(R.string.pref_show_daily_notification_key)
        )
    }

    class NotesDbCommunicator(context: Context) : SQLiteOpenHelper(context, "notesdb", null, 2) {

        override fun onCreate(db: SQLiteDatabase) {
            writableDatabase.execSQL(createTableStatement)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        fun cursorForAll(): Cursor = readableDatabase.query("notes", null, null, null, null, null, null)

        private val createTableStatement = "CREATE TABLE notes ( __id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, language TEXT NOT NULL, book1 TEXT NOT NULL, chapter1 TEXT NOT NULL, verse1 TEXT NOT NULL, id1 TEXT NOT NULL, intro1 TEXT NOT NULL, text1 TEXT NOT NULL, ref1 TEXT NOT NULL, book2 TEXT NOT NULL, chapter2 TEXT NOT NULL, verse2 TEXT NOT NULL, id2 TEXT NOT NULL, intro2 TEXT NOT NULL, text2 TEXT NOT NULL, ref2 TEXT NOT NULL, note TEXT NOT NULL );"

    }

    class WordsDbCommunicator(context: Context) : SQLiteOpenHelper(context, "worddb2017v4", null, 18) {

        override fun onCreate(db: SQLiteDatabase) {
            writableDatabase.execSQL(createTableStatement)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        }

        fun deleteAll() {
            writableDatabase.delete("thewords", null, null)
        }

        private val createTableStatement = "CREATE TABLE thewords ( __id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT NOT NULL, language TEXT NOT NULL, book1 TEXT NOT NULL, chapter1 TEXT NOT NULL, verse1 TEXT NOT NULL, id1 TEXT NOT NULL, intro1 TEXT NOT NULL, text1 TEXT NOT NULL, ref1 TEXT NOT NULL, book2 TEXT NOT NULL, chapter2 TEXT NOT NULL, verse2 TEXT NOT NULL, id2 TEXT NOT NULL, intro2 TEXT NOT NULL, text2 TEXT NOT NULL, ref2 TEXT NOT NULL )  ; "
    }

}