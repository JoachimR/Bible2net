package de.reiss.bible2net.theword.downloader.extensions

import android.database.Cursor

fun Cursor.getLong(column: String): Long = this.getLong(this.getColumnIndex(column))

fun Cursor.getInt(column: String): Int = this.getInt(this.getColumnIndex(column))

fun Cursor.getString(column: String): String = this.getString(this.getColumnIndex(column))
