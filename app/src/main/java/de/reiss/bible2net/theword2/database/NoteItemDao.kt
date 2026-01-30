package de.reiss.bible2net.theword2.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import java.util.Date

@Dao
interface NoteItemDao {

    @Query("SELECT * FROM NoteItem")
    fun all(): List<NoteItem>

    @Query("SELECT * FROM NoteItem WHERE date = :date")
    fun byDate(date: Date): NoteItem?

    @Query("SELECT * FROM NoteItem WHERE date BETWEEN :from AND :to")
    fun range(from: Date, to: Date): List<NoteItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(vararg items: NoteItem): List<Long>

    @Delete
    fun delete(vararg item: NoteItem): Int

    @Query("DELETE FROM NoteItem")
    fun clear()
}
